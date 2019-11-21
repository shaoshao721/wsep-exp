nohup java -jar /app/metrics-fetcher.jar  > fetch.log  2>&1 &
nohup java -jar /app/content-consumer.jar > consumer.log 2>&1 &

# keep running
tail -f /dev/null