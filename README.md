# MQTT-SLA-Blockchain-QoS-Enforcement
The use of Blockchain to enforce violation consequences stipulated in SLA-guaranteed IoT components. In the source code, we make use of an MQTT SLA established by Google Cloud Platform IoT core. We use Hyperledger Fabric as underlying network. We implement the enforcement logic as smart contract and represent the SLA related terms with blockchain. We provide a simulation of a monitoring agent. We conduct a benchmarking using Hyperledger Caliper. 

#
## Compliance Assessment as smart contract
The implementation of the enforcement logic as smart contract. The smart contract is implemented in Java programming language. Find the implementation in the subdirectory:

    /mqttslaEnforce/src/main/java/org/example/


### Prerequisite
* Visual studio Code
* IBM Blockchain Platform Extension
* install all visual Code Code-relevant Java Extensions such as:
    * Debugger for Java
    * Maven
    * RedHat Language support for Java
    *


