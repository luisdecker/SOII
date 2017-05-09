import sys
arq = open("/dev/pts/%s"%sys.argv[2], "w+")
arq.write("%s\r\n"%sys.argv[1])
arq.close()
