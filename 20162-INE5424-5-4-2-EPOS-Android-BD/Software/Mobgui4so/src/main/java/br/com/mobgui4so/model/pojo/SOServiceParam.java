/**
 *
 */
package br.com.mobgui4so.model.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.mobgui4so.model.guigenerating.Gene.ParamType;

/**
 * @author Ercilio Nascimento
 */
public class SOServiceParam implements Serializable {

	private static final long serialVersionUID = 3457815774849515058L;
	private String name;
	private ParamType type;
	private int minValue;
	private int maxValue;
	private String options;
	private int value;
	private String idRegisterModbus;

	public SOServiceParam() {

	}

	public SOServiceParam(String name, String type) {
		this.name = name;
		this.type = ParamType.valueOf(type.toUpperCase());
	}
	public void setValue(int val) {
		value = val;
	}
	public void setValue(String val) {
		int v = 0;
		if (val.isEmpty()) {
			val = "0";
		}
		switch (this.getType()) {
			case BOOLEAN:
				v = val.equals("true") ? 1 : 0;
				break;
			case GET:
			case DOUBLE:
				double d = Double.parseDouble(val);
//				if (d%1 == 0) {
//					// It's safe to parse as int directly..
//					v = Integer.parseInt(val)*100;
//				} else {
					BigDecimal bd = new BigDecimal(d);
					bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					v = (int) (bd.doubleValue()*100);
//				}
				break;
			case COMBO:
				List<String> options = Arrays.asList(this.getOptions().split("\\|"));
				v = options.indexOf(val);
				break;
			case INT:
				v = Integer.parseInt(val);
				break;
		}
		setValue(v);
	}

	public String getValueString() {
        switch (this.type) {
            case GET:
            case DOUBLE:
                return (value/100.0)+"";
            default:
                return value+"";
        }
    }
	public int getTransformedValue() {
		switch (this.type) {
			case GET:
			case DOUBLE:
				return (value/100);
			default:
				return value;
		}
	}
	public int getValue() {
		return this.value;
	}

	public String getName() {
		return name;
	}

	public ParamType getType() {
		return type;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		if (minValue != null && !minValue.equals("") && !minValue.equals("null")) {
			this.minValue = Integer.parseInt(minValue);
		}
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		if (maxValue != null && !maxValue.equals("") && !maxValue.equals("null")) {
			this.maxValue = Integer.parseInt(maxValue);
		}
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(ParamType type) {
		this.type = type;
	}

	public String getIdRegisterModbus() {
		return idRegisterModbus;
	}

	public void setIdRegisterModbus(String idRegisterModbus) {
		this.idRegisterModbus = idRegisterModbus;
	}

}
