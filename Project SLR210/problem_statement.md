## Statement of the problem

We are building a replicated state machine. We aim at solving the consensus problem while the processes taking part in the state machine can fail. However, the number of failure cannot be greater than a known number of processes. 
More precisely, no two correct processes can decide differently, any decided value must have been proposed and we must have an obstruction free termination. Additionally, we hope that every persistent request be answered (we cannot infinitely abort). We cannot guarantee this condition here.
We do not have here the need to implement safety as only one decision is made.
