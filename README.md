jcaptcha-all

Modernized JCaptcha for Java 21 and Jakarta EE 10 (image & audio CAPTCHA)

# JCaptcha (Jakarta / Java 21)

Modernized fork of the original JCaptcha project, updated to work with **Java 21** and **Jakarta EE 10** environments.

This project removes legacy Java EE dependencies and enables the library to run on modern application servers such as **JBoss EAP 8 / WildFly**, **Tomcat 10+**, and other Jakarta-compatible containers.

---

## ✨ Features

- Java 21 compatible  
- Jakarta EE 10 compatible (Servlet API 6+)  
- Image CAPTCHA support  
- Audio CAPTCHA support (FreeTTS)  
- Ehcache 3 integration (removed legacy Ehcache 2)  
- Removed deprecated Java EE APIs  
- Updated dependencies for modern JVM  
- Compatible with Struts 7 (via jcaptcha4struts2-jakarta)  

---

## 📦 Maven Coordinates


<dependency>
  <groupId>com.octo.captcha</groupId>
  <artifactId>jcaptcha-all</artifactId>
  <version>1.2-jakarta</version>
</dependency>


🧩 Typical Usage

This library provides CAPTCHA generation services (image and audio) that can be integrated into:

Jakarta EE web applications

Struts-based applications

Spring MVC / Spring Boot apps (manual integration)

Any Java web application requiring CAPTCHA validation


🔊 Audio CAPTCHA

Audio CAPTCHA is implemented using FreeTTS and generates spoken digit-based verification codes.


🧱 Compatibility
Component			Supported
Java				21
Jakarta EE			10
Servlet API			6.1
WildFly / JBoss		✔
Tomcat 10+			✔
Struts 7			✔ (via adapter)


📜 Origin

This project is based on the original JCaptcha:
👉 https://jcaptcha.sourceforge.net/

The codebase has been modernized for Jakarta EE and recent JVM versions while preserving the original architecture and functionality.

📄 License

This project is released under the MIT License.


👤 Author

Ercole Maria Gullo ( gullo.ercolemaria@gmail.com )
Jakarta modernization, Java 21 compatibility, Ehcache 3 migration, dependency updates, and integration fixes.

🚀 Related Project

Jakarta-compatible Struts integration:
👉 jcaptcha4struts2

🤝 Contributions

Contributions, issues, and pull requests are welcome.


