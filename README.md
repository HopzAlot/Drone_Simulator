# Autonomous Drone Fleet Coordination Simulator

## Introduction
The **Autonomous Drone Fleet Coordination Simulator** is a high-fidelity 3D simulation platform designed to model the collective behavior of multiple quadrotor drones. This project explores how object-oriented programming concepts—such as encapsulation, modularity, inheritance, and composition—can be used to build complex interacting systems from simpler components. 

The simulator provides a safe and efficient way to test coordination and control algorithms, such as formation maintenance and collision avoidance, before hardware implementation in fields like precision agriculture and environmental research.

---

## Key Features
* **Autonomous Agent Modeling**: Each drone operates as an independent autonomous agent capable of movement, target tracking, and communication.
* **Physics-Based Dynamics**: Implements translational and rotational motion using discrete-time Newtonian dynamics and Euler’s rotational equations.
* **Decentralized Coordination**: Features consensus-based formation control where drones adjust motion based on local neighbor information.
* **Safety Protocols**: Utilizes repulsive potential fields to prevent drone overlap and ensure collision avoidance.
* **Stochastic Communication**: Models radio-based data exchange, incorporating spatial distance constraints and probabilistic signal loss.
* **Performance Analytics**: Calculates system-level metrics including average spacing, collision counts, and communication success rates.

---

## System Architecture
The system is designed as a modular and object-oriented architecture where each class corresponds to a distinct physical or computational process.

![UML Diagram](./dronesimulator_uml.webp)

### Core Components
| Class | Description |
| :--- | :--- |
| **Simulator** | The core numerical engine managing time steps, numerical integration, and synchronization. |
| **Drone** | Encapsulates physical parameters (mass, inertia, drag) and bridges the physical model with the software environment. |
| **Controller** | Implements PID/PD control laws to convert positional discrepancies into thrust and torque. |
| **FormationManager** | Maintains spatial coordination and relative positioning within a group. |
| **CollisionAvoidance** | Applies repulsive forces to prevent inter-drone collisions. |
| **CommunicationModule** | Facilitates data exchange among nearby drones based on range constraints. |
| **Environment** | Defines 3D operational boundaries and enforces physical limits of the system. |
| **Logger** | Records telemetry and performance metrics into `positions.csv` and `metrics.txt`. |

---

## Technical Specifications
* **Language**: Java
* **Simulation Model**: Discrete-time steps ($\Delta t$)
* **Physics Model**: Newton-Euler equations for 3D flight
* **Output Formats**: CSV (telemetry) and TXT (performance summaries)

---

## Mathematical Foundations
The simulator relies on several mathematical models to ensure realism:
* **Translational Dynamics**: $ma = mg + RT + F_{aero} + F_{rep} + F_{form}$
* **Rotational Dynamics**: $\dot{\omega} = I^{-1}(\tau - \omega \times (I\omega))$
* **Formation Control**: $F_{form,i} = -\sum_{j \in N_i} [k_p(p_i - p_j) + k_v(v_i - v_j)]$
* **Collision Repulsion**: $F_{rep,i} = \sum_{j \in N_i} k_{rep} \frac{(p_i - p_j)}{\|p_i - p_j\|^2}$
