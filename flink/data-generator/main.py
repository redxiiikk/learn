from time import sleep
from kafka.admin import KafkaAdminClient, NewTopic

if __name__ == "__main__":

    admin = KafkaAdminClient(bootstrap_servers="localhost:9092")
    admin.delete_topics(["word_count"])
    sleep(3)
    admin.create_topics(
        [
            NewTopic("word", 1, 1),
            NewTopic("word_count", 1, 1),
        ]
    )
