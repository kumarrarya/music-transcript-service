#!/bin/bash

export MINIO_NOTIFY_KAFKA_ENABLE_PRIMARY=on
export MINIO_NOTIFY_KAFKA_BROKERS_PRIMARY=localhost:9092
export MINIO_NOTIFY_KAFKA_TOPIC_PRIMARY=audio-upload-events

minio server ~/minio-data --address :9000 --console-address :9001
