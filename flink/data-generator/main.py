from time import sleep
from kafka.admin import KafkaAdminClient, NewTopic

def delete_topic(topics):
    admin = KafkaAdminClient(bootstrap_servers="localhost:9092")
    existed_topics = admin.list_topics()

    for topic in topics:
        if topic in existed_topics:
            admin.delete_topics([topic])


if __name__ == "__main__":
    delete_topic(["vehicle_order"])
