# ✅ Configuración Docker Completa - MasterChef API

## 📦 Archivos Creados

| Archivo | Descripción |
|---------|-------------|
| `Dockerfile` | Imagen Docker multi-stage optimizada |
| `.dockerignore` | Excluye archivos innecesarios del build |
| `docker-compose.yml` | Orquestación local (App + MongoDB) |
| `.github/workflows/cicd.yml` | CI/CD con Docker y Azure Container Registry |
| `DOCKER_SETUP.md` | Documentación completa de Docker |

---

## 🎯 Estado Actual

✅ **Aplicación corriendo** en http://localhost:8080  
✅ **MongoDB funcionando** en puerto 27018 (container interno: 27017)  
✅ **Swagger UI disponible** en http://localhost:8080/swagger-ui/index.html  
✅ **Dockerfiles optimizados** con multi-stage build  
✅ **CI/CD configurado** para Azure Container Registry  

---

## 🚀 Comandos Rápidos

### Ver estado de los contenedores
```powershell
docker-compose ps
```

### Ver logs en tiempo real
```powershell
# Todos los servicios
docker-compose logs -f

# Solo la aplicación
docker-compose logs -f app

# Solo MongoDB
docker-compose logs -f mongodb
```

### Reiniciar servicios
```powershell
docker-compose restart
```

### Detener todo
```powershell
docker-compose down
```

### Detener y eliminar volúmenes (limpieza completa)
```powershell
docker-compose down -v
```

---

## 🌐 Endpoints Disponibles

| Endpoint | Descripción |
|----------|-------------|
| http://localhost:8080 | API Base |
| http://localhost:8080/swagger-ui/index.html | Documentación Swagger |
| http://localhost:8080/v3/api-docs | OpenAPI JSON |

---

## 🔧 Configuración Azure (CI/CD)

### Secretos requeridos en GitHub

Ve a: **Settings → Secrets and variables → Actions → New repository secret**

| Nombre del Secret | Valor | Cómo obtenerlo |
|-------------------|-------|----------------|
| `ACR_LOGIN_SERVER` | `<nombre>.azurecr.io` | Azure Portal → Container Registry → Overview |
| `ACR_USERNAME` | Usuario admin | Azure Portal → Container Registry → Access keys |
| `ACR_PASSWORD` | Contraseña admin | Azure Portal → Container Registry → Access keys |
| `AZURE_CREDENTIALS` | JSON del Service Principal | Ver comando abajo |

### Crear Service Principal para AZURE_CREDENTIALS

```bash
az ad sp create-for-rbac \
  --name "masterchef-github-actions" \
  --role contributor \
  --scopes /subscriptions/<SUBSCRIPTION_ID> \
  --sdk-auth
```

Copia el JSON completo que devuelve y úsalo como valor del secret `AZURE_CREDENTIALS`.

---

## 📊 Flujo CI/CD

1. **Push a main** → GitHub Actions se activa automáticamente
2. **Build Docker Image** → Se construye la imagen con cache optimizado
3. **Push to ACR** → Se sube la imagen a Azure Container Registry
4. **Deploy to Web App** → Se despliega automáticamente en Azure

---

## 🐛 Troubleshooting

### La app no conecta a MongoDB

```powershell
# Verificar que MongoDB está corriendo
docker ps | Select-String "mongodb"

# Ver logs de MongoDB
docker-compose logs mongodb

# Probar conexión desde la app
docker exec -it masterchef-api wget -O- http://mongodb:27017
```

### Puerto 8080 ya está en uso

Edita `docker-compose.yml` y cambia el puerto:
```yaml
ports:
  - "8081:8080"  # Cambia 8081 por el puerto que prefieras
```

### Rebuil completo (limpieza total)

```powershell
docker-compose down -v
docker system prune -a
docker-compose build --no-cache
docker-compose up -d
```

---

## 📝 Próximos Pasos

1. ✅ **Probar la API localmente** - Usa Swagger UI o Postman
2. ⬜ **Crear Azure Container Registry** - Para despliegue en la nube
3. ⬜ **Configurar secretos en GitHub** - Para CI/CD automático
4. ⬜ **Crear Azure Web App** - Para hosting de producción
5. ⬜ **Configurar MongoDB en Azure** - Azure Cosmos DB o MongoDB Atlas

---

## 🎓 Mejoras Opcionales

- [ ] Agregar volúmenes persistentes para desarrollo local
- [ ] Configurar perfiles de Spring (dev, staging, prod)
- [ ] Agregar healthcheck personalizado
- [ ] Implementar logging centralizado
- [ ] Configurar monitoreo con Azure Monitor

---

**¡Tu aplicación MasterChef está lista para usar con Docker! 🎉**
