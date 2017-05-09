package br.com.mobgui4so.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import br.com.mobgui4so.R;
import br.com.mobgui4so.controller.ApplicationFacade;
import br.com.mobgui4so.model.guigenerating.Gene;
import br.com.mobgui4so.model.guigenerating.Genotype;
import br.com.mobgui4so.model.guigenerating.decoder.AndroidDecoder;
import br.com.mobgui4so.model.guigenerating.phenotype.AndroidPhenotype;
import br.com.mobgui4so.model.pojo.SOServiceParam;
import br.com.mobgui4so.model.pojo.SmartObject;
import br.com.mobgui4so.model.pojo.SmartObjectList;
import br.com.mobgui4so.model.interaction.SmartObjectCommandSender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class SmartObjectGUIActivity extends BaseActivity implements OnItemClickListener, ITransaction {

    private ListView listView;
    private SmartObjectList soList;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int idSOClicked;
    private SmartObject so;
    private ApplicationFacade facade;
    private ScrollView soGUILayout;
    private LinearLayout progressLayout;
    private TextView tvProgressMsg;
    private ScrollView layout;
    private boolean flFirstTime;
    private Menu menu;
    private List<String> listParameters;
    private boolean isSendCommand;
    private String ack;
    private static final String PREFS_NAME = "Preferences";
    private AndroidDecoder decoder;
    private Handler mHandler;
    private Double landscapeKey;
    private Double portraitKey;
    private boolean isLandscape;
    private int width;
    private int height;
    private Thread loop;
    private TextView serviceName;
    private SOLinearLayout toRefresh;
    private Map<String, SOServiceParam> toRefreshData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_object_gui);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        Bundle b = getIntent().getExtras();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        this.idSOClicked = settings.getInt("idSOClicked", -1);
        if (this.idSOClicked == -1) {
            this.idSOClicked = b.getInt("idSOClicked", 0);
        }
        try {
            this.soList = new ApplicationFacade().loadSmartObjectListFromDisk(openFileInput("SOFILE"));
        } catch (FileNotFoundException e) {
            // do nothing
        } catch (IOException e) {
            // do nothing
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // this.soList = (SmartObjectList) b.getSerializable("SOList");
        this.so = soList.getList().get(idSOClicked);
        this.landscapeKey = so.getActualLandscapeKey() == null ? (so.getLandscapeGenotypes().isEmpty() ? null : so.getLandscapeGenotypes().lastKey()) : so.getActualLandscapeKey();
        this.portraitKey = so.getActualPortraitKey() == null ? (so.getPortraitGenotypes().isEmpty() ? null : so.getPortraitGenotypes().lastKey()) : so.getActualPortraitKey();
        setTitle(this.soList.getList().get(idSOClicked).getName());
        this.listView = (ListView) findViewById(R.id.smListView);
        this.soGUILayout = (ScrollView) findViewById(R.id.soGUILayout);
        this.progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
        this.tvProgressMsg = (TextView) findViewById(R.id.tvProgressMsg);
        this.tvProgressMsg.setText(getText(R.string.pmGeneratingGUI));
        this.facade = new ApplicationFacade();
        this.decoder = new AndroidDecoder();
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        this.mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                // null,
                // R.drawable.ic_launcher,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        this.soGUILayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                soGUILayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                width = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getWidth();
                height = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
                isLandscape = width > height;
                // getSlidingMenu().setBehindWidth((int) (width * .8));
                if (isLandscape && landscapeKey != null) {
                    decode(so.getLandscapeGenotypes().get(landscapeKey));
                } else if (!isLandscape && portraitKey != null) {
                    decode(so.getPortraitGenotypes().get(portraitKey));
                } else {
                    generateGUI();
                }
            }
        });
        setSlidingMenu();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        menu.findItem(R.id.refresh_option).setEnabled(true);
                        verifyMenuItemsStatus();
                        break;
                    case 3:
                        makeToast((String) msg.obj);
                    case 5:
                        refreshInterface(toRefreshData, toRefresh);
                        makeToast("Dados atualizados com sucesso!");
                    default:
                        break;
                }
            }
        };
    }

    private void setSlidingMenu() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.soList.getSONames());
        this.listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (this.so != this.soList.getList().get(arg2)) {
            this.idSOClicked = arg2;
            this.so = this.soList.getList().get(this.idSOClicked);
            this.landscapeKey = so.getActualLandscapeKey() == null ? (so.getLandscapeGenotypes().isEmpty() ? null : so.getLandscapeGenotypes().lastKey()) : so.getActualLandscapeKey();
            this.portraitKey = so.getActualPortraitKey() == null ? (so.getPortraitGenotypes().isEmpty() ? null : so.getPortraitGenotypes().lastKey()) : so.getActualPortraitKey();
            setTitle(this.so.getName());
            if (isLandscape && landscapeKey != null) {
                decode(so.getLandscapeGenotypes().get(landscapeKey));
            } else if (!isLandscape && portraitKey != null) {
                decode(so.getPortraitGenotypes().get(portraitKey));
            } else {
                generateGUI();
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(listView);
        // menu.findItem(R.id.previous_gui).setVisible(!drawerOpen);
        // menu.findItem(R.id.next_gui).setVisible(!drawerOpen);
        menu.findItem(R.id.refresh_option).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.guimenu, menu);

        this.menu = menu;
        verifyMenuItemsStatus();

        return super.onCreateOptionsMenu(menu);
    }

    private void verifyMenuItemsStatus() {
        if (isLandscape) {
            if (landscapeKey == null) {
                setEnabledMenuItem(R.id.previous_gui, R.drawable.ic_action_previous_item_disabled, false);
                setEnabledMenuItem(R.id.next_gui, R.drawable.ic_action_next_item_disabled, false);
            } else {
                if (so.getLandscapeGenotypes().lowerKey(landscapeKey) == null) {
                    setEnabledMenuItem(R.id.next_gui, R.drawable.ic_action_next_item_disabled, false);
                } else {
                    setEnabledMenuItem(R.id.next_gui, R.drawable.ic_action_next_item_enabled, true);
                }
                if (so.getLandscapeGenotypes().higherKey(landscapeKey) == null) {
                    setEnabledMenuItem(R.id.previous_gui, R.drawable.ic_action_previous_item_disabled, false);
                } else {
                    setEnabledMenuItem(R.id.previous_gui, R.drawable.ic_action_previous_item_enabled, true);
                }
            }
        } else {
            if (portraitKey == null) {
                setEnabledMenuItem(R.id.previous_gui, R.drawable.ic_action_previous_item_disabled, false);
                setEnabledMenuItem(R.id.next_gui, R.drawable.ic_action_next_item_disabled, false);
            } else {
                if (so.getPortraitGenotypes().lowerKey(portraitKey) == null) {
                    setEnabledMenuItem(R.id.next_gui, R.drawable.ic_action_next_item_disabled, false);
                } else {
                    setEnabledMenuItem(R.id.next_gui, R.drawable.ic_action_next_item_enabled, true);
                }
                if (so.getPortraitGenotypes().higherKey(portraitKey) == null) {
                    setEnabledMenuItem(R.id.previous_gui, R.drawable.ic_action_previous_item_disabled, false);
                } else {
                    setEnabledMenuItem(R.id.previous_gui, R.drawable.ic_action_previous_item_enabled, true);
                }
            }
        }
    }

    private void setEnabledMenuItem(int itemId, int iconId, boolean enabled) {
        this.menu.findItem(itemId).setEnabled(enabled);
        this.menu.findItem(itemId).setIcon(iconId);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.refresh_option) {
            generateGUI();
        } else if (item.getItemId() == R.id.next_gui) {
            if (isLandscape) {
                Double nextLandscapeKey = so.getLandscapeGenotypes().lowerKey(landscapeKey);
                landscapeKey = nextLandscapeKey;
                so.setActualLandscapeKey(nextLandscapeKey);
                decode(so.getLandscapeGenotypes().get(nextLandscapeKey));
            } else {
                Double nextPortraitKey = so.getPortraitGenotypes().lowerKey(portraitKey);
                portraitKey = nextPortraitKey;
                so.setActualPortraitKey(nextPortraitKey);
                decode(so.getPortraitGenotypes().get(nextPortraitKey));
            }
        } else if (item.getItemId() == R.id.previous_gui) {
            if (isLandscape) {
                Double previousLandscapeKey = so.getLandscapeGenotypes().higherKey(landscapeKey);
                landscapeKey = previousLandscapeKey;
                so.setActualLandscapeKey(previousLandscapeKey);
                decode(so.getLandscapeGenotypes().get(previousLandscapeKey));
            } else {
                Double previdousPortraitKey = so.getPortraitGenotypes().higherKey(portraitKey);
                portraitKey = previdousPortraitKey;
                so.setActualPortraitKey(previdousPortraitKey);
                decode(so.getPortraitGenotypes().get(previdousPortraitKey));
            }
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("idSOClicked", this.idSOClicked);
        editor.commit();
        if (loop != null) {
            loop.interrupt();
        }
        Thread save = new Thread(new Runnable() {
            public void run() {
                try {
                    facade.saveSmartObjectListToDisk(soList, openFileOutput("SOFILE", Context.MODE_PRIVATE));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        save.start();
        try {
            save.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected Map<String, SOServiceParam> refreshData(SmartObject so, String serviceName) {
        String response = facade.sendSmartObjectRequest(so, serviceName);
        System.out.println("Resposta: '"+response+"'");

        String[] data = response.split(";");

        Map<String, SOServiceParam> results = new HashMap<>();

        for (int c=0; c < data.length; c++) {
            String[] row = data[c].split(",");
            SOServiceParam p = so.getParamByIDRegister(row[0]);
            if (p == null) {
                // Not found, continue trying..
                continue;
            }
            try {
                p.setValue(Integer.parseInt(row[1]));
                results.put(row[0], p);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private void refreshInterface(Map<String, SOServiceParam> info, View root) {
        int i = 0;
        if (this.flFirstTime) {
            i = 1;
            this.flFirstTime = false;
        }
        if (!this.flFirstTime && !(root instanceof LinearLayout)) {
            String registerModBusTag;
            boolean isFieldLabel;
            try {
                isFieldLabel = (boolean) root.getTag(R.id.fieldIsLabel);
            } catch (Exception ex) {
                ex.printStackTrace();
                isFieldLabel = false;
            }
            try {
                registerModBusTag = (String) root.getTag(R.id.registerModBusTag);
            } catch (Exception ex) {
                ex.printStackTrace();
                registerModBusTag = null;
            }
            if (isFieldLabel && registerModBusTag == null) {
                // Probably a label
                return;
            }
            if (info == null || !info.containsKey(registerModBusTag)) {
                return;
            }
            SOServiceParam p = info.get(registerModBusTag);
            System.out.println(p.getValue());
            if (root instanceof CheckBox) {
                ((CheckBox) root).setChecked(p.getValue() == 1);
            } else if (root instanceof EditText) {
                ((EditText) root).setText(p.getValueString());
            } else if (root instanceof TextView) {
                ((TextView) root).setText(p.getValueString());
            } else if (root instanceof Spinner) {
                ((Spinner) root).setSelection(p.getTransformedValue());
            } else if (root instanceof NegativeSeekBar) {
                ((NegativeSeekBar) root).setProgress(p.getTransformedValue()-p.getMinValue());
            } else if (root instanceof RadioGroup) {
                RadioGroup rg = (RadioGroup) root;
                rg.check(p.getValue());
            }
            if (root instanceof View) {
                ((View) root).refreshDrawableState();
            }


            return;
        } else {
            for (; i < ((LinearLayout) root).getChildCount(); i++) {
                refreshInterface(info, ((LinearLayout) root).getChildAt(i));
            }

            return;
        }

    }


    public void refreshService(final View view) {
        this.toRefresh = (SOLinearLayout) view.getParent().getParent();
        final String sN = String.valueOf(((TextView) ((LinearLayout) toRefresh.getChildAt(0)).getChildAt(1)).getText());
        new Thread(new Runnable() {
            @Override
            public void run() {
                toRefreshData = refreshData(so, sN);
                if (toRefreshData.isEmpty()) {
                    mHandler.obtainMessage(3, "Houve um problema durante o carregamento dos dados").sendToTarget();
                } else {
                    mHandler.obtainMessage(5).sendToTarget();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.idSOClicked = -1;
        if (loop != null) {
            loop.interrupt();
        }
    }

    public void generateGUI() {
        startTransaction(this);
    }

    private void decode(final Genotype genotype) {
        new Thread(new Runnable() {
            public void run() {
                AndroidPhenotype phenotype = (AndroidPhenotype) decoder.decode(genotype, SmartObjectGUIActivity.this);
                mHandler.obtainMessage(1, phenotype).sendToTarget();
            }
        }).start();
    }

    @Override
    public void preExecute() {
        // swapLayouts();
    }

    @Override
    public void execute() throws Exception {
        if (this.isSendCommand) {
            ack = facade.sendSmartObjectCommand(so.getUrl()+"/soCommand.do", this.listParameters);
        } else {
            AndroidPhenotype phenotype = (AndroidPhenotype) this.facade.generateGUI(this.soList, this.idSOClicked, width, height, this, openFileOutput("SOFILE", Context.MODE_PRIVATE));
            this.layout = phenotype.getLayout();
            if (isLandscape) {
                landscapeKey = this.so.getLandscapeGenotypes().lastKey();
            } else {
                portraitKey = this.so.getPortraitGenotypes().lastKey();
            }
            ack = "Tela gerada com sucesso!";
        }
    }

    @Override
    public void postExecute() {
        if (this.isSendCommand) {
            this.isSendCommand = false;
        } else {
            // setEnabledMenuItem(R.id.refresh_option, R.drawable.ic_action_refresh_enabled, true);
            verifyMenuItemsStatus();
            setLayout(this.layout);
            // swapLayouts();
        }
        Toast.makeText(this, ack, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void swapLayouts() {
        this.progressLayout.setVisibility(this.progressLayout.getVisibility() == LinearLayout.VISIBLE ? LinearLayout.INVISIBLE : LinearLayout.VISIBLE);
    }

    public void setLayout(final View layout) {
        ViewGroupUtils.replaceView(soGUILayout, layout);
        layout.invalidate();
        soGUILayout = (ScrollView) layout;
    }

    public void executeService(final View view) {
        final SOLinearLayout frame = (SOLinearLayout) view.getParent().getParent();
        serviceName = (TextView) ((LinearLayout) frame.getChildAt(0)).getChildAt(1);
        this.listParameters = new ArrayList<String>();
        this.listParameters.add("idSOModbus");
        this.listParameters.add(so.getIdSOModbus());
        // this.listParameters.add("idServiceModbus");
        // this.listParameters.add(so.getIdServiceModbus(String.valueOf(serviceName.getText())));
        // this.listParameters.add("idRegisterModbus");
        // this.listParameters.add(so.getIdRegisterModbus(serviceName.getText().toString()));
        // this.listParameters.add(serviceName.getText().toString());
        this.flFirstTime = true;
        createServiceURL(so, this.listParameters, frame);


        this.isSendCommand = true;
        serviceName = null;
        // if (listParameters.get(3).equals("03")) {
        //     serviceName = getAckTextView();
        // }
        new Thread(new Runnable() {
            public void run() {
                ack = facade.sendSmartObjectCommand(so.getUrl()+"/soCommand.do", listParameters);
                // if (serviceName == null) {
                    mHandler.obtainMessage(3, ack).sendToTarget();
                // } else {
                //    mHandler.obtainMessage(2, ack).sendToTarget();
                //}
            }
        }).start();
        // startTransaction(this);
    }

    /**
     * @return
     */
    // private TextView getAckTextView() {
    //     TextView tv = null;
    //     LinearLayout layout = (LinearLayout) soGUILayout.getChildAt(0);
    //     for (int i = 0; i < layout.getChildCount(); i++) {
    //         View view = layout.getChildAt(i);
    //         if (view instanceof SOLinearLayout && ((SOLinearLayout) view).isGetLayout()) {
    //             LinearLayout child = (LinearLayout) ((LinearLayout) view).getChildAt(1);
    //             tv = (TextView) child.getChildAt(1);
    //         }
    //     }
    //
    //     return tv;
    // }

    private void makeToast(String message) {
        Toast.makeText(SmartObjectGUIActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void createServiceURL(SmartObject smartObject, List<String> list, View root) {
        int i = 0;
        if (this.flFirstTime) {
            i = 1;
            this.flFirstTime = false;
        }
        if (!this.flFirstTime && !(root instanceof LinearLayout)) {
            String registerModBusTag;
            boolean isFieldLabel;
            try {
                isFieldLabel = (boolean) root.getTag(R.id.fieldIsLabel);
            } catch (Exception ex) {
                ex.printStackTrace();
                isFieldLabel = false;
            }
            try {
                registerModBusTag = (String) root.getTag(R.id.registerModBusTag);
            } catch (Exception ex) {
                ex.printStackTrace();
                registerModBusTag = null;
            }
            if (isFieldLabel && registerModBusTag == null) {
                // Probably a label
                return;
            }
            SOServiceParam param = so.getParamByIDRegister(registerModBusTag);
            if (param.getType() == Gene.ParamType.GET) {
                // Do not send read-only values..it does not make sense to do that..
                return;
            }
            if (registerModBusTag == null) {
                registerModBusTag = "unknown";
            }
            list.add(registerModBusTag);
            if (root instanceof CheckBox) {
                param.setValue(String.valueOf(((CheckBox) root).isChecked()));
            } else if (root instanceof EditText) {
                param.setValue(((EditText) root).getText().toString());
            } else if (root instanceof TextView) {
                String tv = ((TextView) root).getText().toString();
                if (tv.contains(":")) {
                    param.setValue(tv.substring(0, tv.indexOf(" ")));
                } else {
                    param.setValue(tv);
                }
            } else if (root instanceof Spinner) {
                param.setValue((String) ((Spinner) root).getSelectedItem());
            } else if (root instanceof NegativeSeekBar) {
                param.setValue(((NegativeSeekBar) root).getStringProgress());
            } else if (root instanceof RadioGroup) {
                RadioGroup rg = (RadioGroup) root;
                param.setValue(rg.getCheckedRadioButtonId());
            }
            list.add(param.getValue()+"");
// 11-07 22:12:48.976 10157-11031/br.com.mobgui4so I/System.out: idSOModbus=A1&3=0&6=0&7=-1&8=0&

            return;
        } else {
            for (; i < ((LinearLayout) root).getChildCount(); i++) {
                createServiceURL(smartObject, list, ((LinearLayout) root).getChildAt(i));
            }

            return;
        }

    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
