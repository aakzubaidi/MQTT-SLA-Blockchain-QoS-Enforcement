# MQTT-SLA-Blockchain-QoS-Enforcement
The use of Blockchain to enforce violation consequences stipulated in SLAs that guarantee the quality of IoT services and components. In the source code, we make use of an [MQTT SLA](https://cloud.google.com/iot/sla) established by Google Cloud Platform IoT core. We use Hyperledger Fabric as underlying network. We implement the enforcement logic as smart contract and represent the SLA related terms with blockchain. We provide a simulation of a monitoring agent. We conduct a benchmarking using Hyperledger Caliper. 


## Compliance Assessment as smart contract
The implementation of the enforcement logic as smart contract. The smart contract is implemented in Java programming language. Find the implementation in the subdirectory:

    /mqttslaEnforce/src/main/java/org/example/


### Prerequisite
* Visual studio Code
* IBM Blockchain Platform Extension. Install all required components requested by this extension.
* VS code Docker extension
* Go By microsoft
* install all visual Code Code-relevant Java Extensions such as:
    * Debugger for Java
    * Maven
    * RedHat Language support for Java
    * Java Test Runner
    * Java Extension Pack by Microsoft
    * Java dependency viewer
    * Gradle Language support

### Deploying and running the smart contract

Follow the instructions described 
[in this Youtube video](https://www.youtube.com/watch?v=eTRkADi3y6Q)



