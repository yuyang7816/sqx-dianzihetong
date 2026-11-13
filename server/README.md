**电子合同服务端（e-contract）**

基于 Spring Boot 的前后端分离接口服务，为管理端与用户端提供合同、企业、印章、支付、系统权限等能力。

**本地启动前**

1. 创建数据库 `dianzihetong`，导入仓库根目录 `db/dianzihetong.sql`
2. 修改 `src/main/resources/application-dev.yml` 中的 MySQL 账号密码
3. 确认 `application.yml` 中 `spring.profiles.active=dev`，端口默认 `9131`，路径 `/sqx_fast`
4. 如需本地 jar，参考：

```bash
mvn install:install-file -Dfile=ilb/jave-1.0.2.jar -DgroupId=it.sauronsoftware -DartifactId=jave -Dversion=1.0.2 -Dpackaging=jar
```

（按 `ilb` 目录实际文件调整路径）

5. 运行 `com.sqx.SqxApplication`，控制台出现「电子合同项目启动成功」

演示管理员：`admin` / `admin123`（来自开源 SQL，上线请改密）

更完整的步骤见仓库根目录 [docs/服务端-运行教程.md](../docs/服务端-运行教程.md)。
