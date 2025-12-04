# 📱 AppMensajeria

Aplicación de mensajería en tiempo real desarrollada en **Java para Android**, con **Firebase Authentication + Realtime Database**, almacenamiento local con **Room**, y transmisión de mensajes mediante **Broker MQTT**, permitiendo comunicación fluida entre usuarios con perfiles personalizables.

---

## 📝 Descripción General

**AppMensajeria** es una aplicación móvil enfocada en la comunicación instantánea.  
Permite que los usuarios se registren, inicien sesión, envíen mensajes, personalicen su perfil (incluyendo foto de perfil) y reciban mensajes en tiempo real mediante Firebase + MQTT.

El sistema combina sincronización en la nube con una base de datos local para uso offline, mejorando rendimiento y experiencia del usuario.

---

## 🚀 Tecnologías Utilizadas

| Componente | Uso principal |
|-----------|----------------|
| **Java (Android)** | Desarrollo de la app |
| **Firebase Authentication** | Registro/Login seguro |
| **Firebase Realtime Database** | Almacenamiento de usuarios y mensajes |
| **Room (SQLite ORM)** | Cache local y soporte offline |
| **Broker MQTT (HiveMQ/HivemqClient)** | Mensajes instantáneos entre usuarios |
| **Android Architecture Components** | Manejo de ciclos de vida y persistencia |

---

## 🔥 Funcionalidades Principales

- 🔐 Registro e inicio de sesión con Firebase Authentication  
- ☁ Almacenamiento de usuarios y chats en Firebase Realtime Database  
- 💬 Envío y recepción de mensajes entre usuarios
- 🌐 Comunicación en tiempo real con **MQTT Broker**
- 🖼 Perfiles modificables: nombre, estado y fotografía de perfil
- 📦 Base de datos local mediante Room para historial y carga offline
- 🔄 Sincronización cloud ↔ local automática
- 🟢 Estado conectado/no conectado (según MQTT)

---

## 📦 Estructura del Proyecto

