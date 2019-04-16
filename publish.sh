#! /bin/bash

ls -l target/
# stat -c 在mac os 无法使用
# jarBuildTime=`stat -c %Y target/shoppingstreet-0.0.1-SNAPSHOT.jar`
# nowTime=`date +%s`
# timeOut=`expr $nowTime - $jarBuildTime`
# if [ $timeOut -gt 600 ]; then
#     echo "time out"
#     exit
# fi


# eeooff 自定义的远程脚本结束符号

sshpass -p $1 ssh -tt -p 22 root@203.195.176.170 << eeooff
sudo ./shutdown_shoppingstreet.sh
rm -f ./shoppingstreet-0.0.1-SNAPSHOT.jar
exit
eeooff


sshpass -p $1 scp ./target/shoppingstreet-0.0.1-SNAPSHOT.jar root@203.195.176.170:/root/



sshpass -p $1 ssh -tt -p 22 root@203.195.176.170 << eeooff
java -version
./shoppingstreet_run.sh
exit
eeooff



echo done!
