from time import sleep
from kafka.admin import KafkaAdminClient, NewTopic


def recreate_topic(topics):
    admin = KafkaAdminClient(bootstrap_servers="localhost:9092")
    existed_topics = admin.list_topics()

    for topic in topics:
        if topic in existed_topics:
            admin.delete_topics([topic])

    sleep(3)

    admin.create_topics([NewTopic(topic, 1, 1) for topic in topics])


if __name__ == "__main__":
    recreate_topic(
        [
            "__consumer_offsets",
            "word",
            "cc_payments",
            "cc_payments_merchant_statistics",
            "vehicle_order",
            "vehicle_location",
            "vehicle_mileage_statistics",
        ]
    )
