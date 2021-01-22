#!/bin/bash
echo 'start'
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

dbuser=root
dbpassword=sumavisionrd

mysql -u${dbuser} -p${dbpassword} <<EOF
use capacity;
source $DIR/capacity_template.sql;
EOF

echo 'finish'

