#!/usr/bin/env bash

source $(dirname "$(readlink -f "$0")")/profile.sh

PROFILE_AND_PORT=$(find_profile_and_port)
CURRENT_PROFILE=$(echo "${PROFILE_AND_PORT}" | awk '{print $1}')
CURRENT_PORT=$(echo "${PROFILE_AND_PORT}" | awk '{print $2}')
IDLE_PROFILE=$(echo "${PROFILE_AND_PORT}" | awk '{print $3}')
IDLE_PORT=$(echo "${PROFILE_AND_PORT}" | awk '{print $4}')

for cnt in {1..10}
do
    echo "서버 응답 대기중 ... (${cnt}/10)";

    UP=$(curl -s http://${IMAGE_NAME | PROJECT_NAME}-${CURRENT_PROFILE}:${CURRENT_PORT}/api/v1/health | grep 'blue\|green')

    if [ -z "${UP}" ]
        then
            sleep 10
            continue
    else
        break
    fi
done

if [ $cnt -eq 10 ]
then
    echo "서버가 정상적으로 구동되지 않았습니다."
    exit 1
fi

echo "> Docker Nginx: Change profile and port - Reload"
echo "set \$service_url http://${IMAGE_NAME}-${CURRENT_PROFILE}:${CURRENT_PORT};" \
| sudo tee ${ABS_PATH}/data/nginx/conf.d/service-url.inc \
&& sudo docker cp ${ABS_PATH}/data/nginx/conf.d/service-url.inc nginx:/etc/nginx/conf.d/service-url.inc \
&& sudo docker exec nginx nginx -s reload

echo "배포가 정상적으로 완료되었습니다. ${IMAGE_NAME}-${CURRENT_PROFILE}:${CURRENT_PORT}"

echo "spring-$IDLE_PROFILE:$IDLE_PORT down"
sudo docker-compose -p ${IMAGE_NAME}-${IDLE_PROFILE} ./docker-compose.${IDLE_PROFILE}.yml down
