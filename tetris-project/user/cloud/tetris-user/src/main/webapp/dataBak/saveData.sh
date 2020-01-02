#!/bin/sh
#INSTALLDIR=${1}
#INSTALLDIR=/usr/sbin/sumavision/tetris
#第一个参数：文件名（带扩展名，且必须是.tar.gz）
#第二个参数：目录名
INSTALLDIR=$(cd `dirname $0`; pwd)
fileName=$1
pathName=$2
mysqlUsername=root
mysqlPassword=sumavisionrd

echo $INSTALLDIR
INSTALLDIR=/usr/sbin/sumavision/tetris/tetris-project/user/apache-tomcat-7.0.93/webapps/ROOT/dataBak
cd $INSTALLDIR

mkdir -p $pathName
cd ./$pathName
INSTALLDIR=$INSTALLDIR/$pathName
mkdir -p data
cd ./data
#echo "copy cms files..."
#cp -rf $INSTALLDIR/tetris-project/cms/apache-tomcat-7.0.93/webapps/ROOT/cms/* ./
#echo "copy mims files..."
#cp -rf $INSTALLDIR/tetris-project/mims/apache-tomcat-7.0.93/webapps/ROOT/upload/* ./
#echo "copy user files..."
#mkdir -p logos
#cp -rf $INSTALLDIR/tetris-project/user/apache-tomcat-7.0.93/webapps/ROOT/logos/* ./logos/

echo "dump cms tables..."
mysqldump -u$mysqlUsername -p$mysqlPassword cms>$INSTALLDIR/data/cms.sql
echo "dump mims tables..."
mysqldump -u$mysqlUsername -p$mysqlPassword mims>$INSTALLDIR/data/mims.sql
echo "dump user tables..."
mysqldump -u$mysqlUsername -p$mysqlPassword user>$INSTALLDIR/data/user.sql
echo "dump easy-process tables..."
mysqldump -u$mysqlUsername -p$mysqlPassword easy_process>$INSTALLDIR/data/easy_process.sql
echo "dump menu tables..."
mysqldump -u$mysqlUsername -p$mysqlPassword menu>$INSTALLDIR/data/menu.sql
echo "dump cs tables..."
mysqldump -u$mysqlUsername -p$mysqlPassword cs>$INSTALLDIR/data/cs.sql
echo "dump p2p tables..."
mysqldump -u$mysqlUsername -p$mysqlPassword p2p>$INSTALLDIR/data/p2p.sql
echo "dump capacity tables..."
mysqldump -u$mysqlUsername -p$mysqlPassword capacity>$INSTALLDIR/data/capacity.sql

echo "tar files..."
cd $INSTALLDIR
tar -vcf $fileName data/
rm -rf data/
echo "finish"
