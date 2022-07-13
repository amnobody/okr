#!/usr/bin/env bash
ssh chenjiwei01 > /dev/null 2>&1 << eeooff
sh okr.sh
exit
eeooff
echo done!
