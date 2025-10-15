# MasterChef API - Docker Setup

## 📋 Archivos creados

✅ **Dockerfile** - Construcción optimizada multi-stage
✅ **.dockerignore** - Excluye archivos innecesarios
✅ **docker-compose.yml** - Orquestación local con MongoDB
✅ **cicd.yml** - CI/CD con Docker y Azure Container Registry

---

## 🚀 Uso Local con Docker

### 1. Construir y ejecutar con Docker Compose

```bash
# Iniciar todos los servicios (MongoDB + API)
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v
```

### 2. Construir solo la imagen Docker

```bash
# Construir imagen
docker build -t masterchef-api:latest .

# Ejecutar contenedor (requiere MongoDB corriendo)
docker run -d -p 8080:8080 \
  -e MONGODB_URI="mongodb://localhost:27017/MASTERCHEF" \
  masterchef-api:latest
```

### 3. Probar la API

```bash
# Verificar que está corriendo
curl http://localhost:8080/actuator/health

# Ver documentación Swagger
http://localhost:8080/swagger-ui.html
```

---

## ☁️ Despliegue en Azure

### Secretos necesarios en GitHub

Configura estos secretos en: **Settings → Secrets and variables → Actions**

| Secret | Descripción | Cómo obtenerlo |
|--------|-------------|----------------|
| `ACR_LOGIN_SERVER` | URL del Container Registry | `<tu-acr>.azurecr.io` |
| `ACR_USERNAME` | Usuario del ACR | Portal Azure → ACR → Access keys |
| `ACR_PASSWORD` | Contraseña del ACR | Portal Azure → ACR → Access keys |
| `AZURE_CREDENTIALS` | Service Principal JSON | `az ad sp create-for-rbac` |

### Crear Azure Container Registry

```bash
# Variables
RESOURCE_GROUP="masterchef-rg"
ACR_NAME="masterchefacr"
LOCATION="eastus"

# Crear grupo de recursos
az group create --name $RESOURCE_GROUP --location $LOCATION

# Crear Container Registry
az acr create \
  --resource-group $RESOURCE_GROUP \
  --name $ACR_NAME \
  --sku Basic \
  --admin-enabled true

# Obtener credenciales
az acr credential show --name $ACR_NAME
```

### Crear Azure Web App para Containers

```bash
APP_NAME="masterchef-web-app"
APP_SERVICE_PLAN="masterchef-plan"

# Crear App Service Plan (Linux)
az appservice plan create \
  --name $APP_SERVICE_PLAN \
  --resource-group $RESOURCE_GROUP \
  --is-linux \
  --sku B1

# Crear Web App
az webapp create \
  --resource-group $RESOURCE_GROUP \
  --plan $APP_SERVICE_PLAN \
  --name $APP_NAME \
  --deployment-container-image-name $ACR_NAME.azurecr.io/masterchef-api:latest

# Configurar variables de entorno
az webapp config appsettings set \
  --resource-group $RESOURCE_GROUP \
  --name $APP_NAME \
  --settings \
    MONGODB_URI="<tu-mongodb-connection-string>" \
    WEBSITES_PORT=8080
```

---

## 🔧 Características del Dockerfile

- ✅ **Multi-stage build** - Reduce tamaño de imagen final
- ✅ **Alpine Linux** - Imagen base ligera
- ✅ **Usuario no-root** - Mejor seguridad
- ✅ **Health check** - Monitoreo automático
- ✅ **Optimización de caché** - Build más rápido

---

## 📊 Flujo CI/CD

1. **Push a main** → Trigger automático
2. **Build Docker image** → Construcción optimizada
3. **Push to ACR** → Almacenamiento en Azure
4. **Deploy to Web App** → Despliegue automático

---

## 🐛 Troubleshooting

### La app no inicia en Docker

```bash
# Ver logs del contenedor
docker logs masterchef-api

# Verificar variables de entorno
docker exec masterchef-api env | grep MONGODB
```

### MongoDB no conecta

```bash
# Verificar que MongoDB está corriendo
docker ps | grep mongodb

# Probar conexión desde el contenedor de la app
docker exec -it masterchef-api wget -O- http://mongodb:27017
```

### Build falla en GitHub Actions

- Verificar que todos los secretos están configurados
- Revisar logs en la pestaña "Actions"
- Asegurar que ACR admin está habilitado
