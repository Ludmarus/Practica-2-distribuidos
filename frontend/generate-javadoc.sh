#!/bin/bash
# Script para generar la documentación Javadoc del proyecto

echo "Generando Javadoc..."
mvn javadoc:javadoc

echo "Documentación generada en target/site/apidocs"
