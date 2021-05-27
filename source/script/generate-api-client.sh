# This script uses openapi-generator-cli
# It can be installed via "npm install @openapitools/openapi-generator-cli -g"
openapi-generator-cli generate -i http://localhost:9001/docs \
-o ../frontend/src/rest -g typescript-axios \
--additional-properties=nullSafeAdditionalProps=true,supportsES6=true,withInterfaces=true,withSeparateModelsAndApi=true,withoutPrefixEnums=true \
--api-package=api --model-package=dto