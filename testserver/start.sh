
#!/bin/bash
cd "$(dirname "$0")"

    java -Xmx2G -Xms2G -XX:+AlwaysPreTouch -XX:+DisableExplicitGC \
        -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:MaxGCPauseMillis=50 \
        -XX:TargetSurvivorRatio=90 -XX:G1NewSizePercent=50 -XX:G1MaxNewSizePercent=80 \
        -XX:InitiatingHeapOccupancyPercent=10 -XX:G1MixedGCLiveThresholdPercent=50 \
        -jar paper-1.20.jar nogui
	 
