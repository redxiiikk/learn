# diagram.py
from diagrams import Diagram, Cluster, Edge
from diagrams.k8s.podconfig import ConfigMap
from diagrams.k8s.compute import Deployment, Job, Pod
from diagrams.k8s.infra import Master
from diagrams.k8s.controlplane import Kubelet

from diagrams.onprem.container import Docker

from diagrams.onprem.ci import GithubActions
from diagrams.onprem.vcs import Github
from diagrams.onprem.client import Users
from diagrams.alibabacloud.compute import ECS

with Diagram(filename="k8s wasm faas", show=False):

    dashed = Edge(color="firebrick", style="dashed")

    develops = Users("devs")
    
    kubectl_plugin_ci = GithubActions("kubectl plugin ci")
    builder_ci = GithubActions("builder ci")
    builder_controller_ci = GithubActions("builder controller ci")

    with Cluster("k8s Cluster"):
        with Cluster("docker kubelet"):
            builder_controller = Deployment("builder controller")
            builder_jobs = Job("builer")
        
        # wasm_runtime_kubelet = Kubelet("wasm runtime")
        master = Master("master")
            


    # kubectl plugin
    develops >> Github("kubectl plugin repo") >> kubectl_plugin_ci >> master
    
    # builder controller
    develops >> Github("builder controller repo") >> builder_controller_ci >> Docker("builder controller image") >> dashed >> builder_controller << builder_controller_ci

    # builder
    develops >> Github("builder repo") >> builder_ci >> Docker("builder image") >> dashed >> builder_jobs << builder_ci