echo 'N6uX$iRBo7Es45jEmtET'
## 同步依赖包
scp -r ./mkt-api/target/thin/root/repository root@10.20.217.41:/opt/service/mkt-api/m2/repository
## 同步核心包
scp -r ./mkt-api/target/thin/root/mkt-api-1.0.jar root@10.20.217.41:/opt/service/mkt-api



