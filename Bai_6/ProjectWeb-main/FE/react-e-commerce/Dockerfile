FROM node:20-bullseye-slim

WORKDIR /app

COPY package.json ./
# Force optional deps (rollup native) to be installed in Linux container
ENV NPM_CONFIG_OPTIONAL=true
# npm 11.3+ fixes optional-deps bug for rollup native binaries
RUN npm install -g npm@11.3.0 \
  && npm install --include=optional --package-lock=false

COPY . .

EXPOSE 5173

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
