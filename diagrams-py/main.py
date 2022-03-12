# diagram.py
from diagrams import Diagram, Cluster, Edge
from diagrams.k8s.compute import Deployment, Job, Pod
from diagrams.k8s.infra import Master
from diagrams.k8s.controlplane import Kubelet

from diagrams.onprem.container import Docker

from diagrams.onprem.ci import GithubActions
from diagrams.onprem.vcs import Github
from diagrams.onprem.client import Users
from diagrams.alibabacloud.compute import ECS


graph_attr = {"splines": "spline"}

with Diagram(filename="k8s wasm faas", show=False, graph_attr=graph_attr):

    dashed = Edge(color="firebrick", style="dashed")

    develops = Users("devs")

    kubectl_plugin_ci = GithubActions("kubectl plugin ci")
    builder_ci = GithubActions("builder ci")
    builder_controller_ci = GithubActions("builder controller ci")

    with Cluster("dockerhub"):
        builder_image = Docker("builder image")
        builder_controller_image = Docker("builder controller image")

    with Cluster("k8s Cluster", direction="TB"):
        master = Master("master")

        with Cluster("wasm runtime"):
            wasm_runtime_kubelet = Kubelet("wasm runtime")

        with Cluster("docker kubelet"):
            builder_controller = Deployment("builder controller")
            builder_jobs = Job("builer")

            builder_controller >> Edge(label="create") >> builder_jobs

    # kubectl plugin
    develops >> Github("kubectl plugin repo") >> kubectl_plugin_ci >> master

    # builder controller
    (
        develops
        >> Github("builder controller repo")
        >> builder_controller_ci
        >> builder_controller_image
        >> dashed
        >> builder_controller
        << builder_controller_ci
    )

    # builder
    (
        develops
        >> Github("builder repo")
        >> builder_ci
        >> builder_image
        >> dashed
        >> builder_jobs
        << builder_ci
    )

    # wasm runtime
    (
        develops
        >> Github("wasm runtime")
        >> GithubActions("wasm runtime kubelet")
        >> wasm_runtime_kubelet
    )
