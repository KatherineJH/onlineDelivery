FROM node:18

WORKDIR /app

COPY package.json ./
COPY package-lock.json ./

# node_modules 없이 깨끗하게 install
RUN npm install

COPY . .

EXPOSE 5173

CMD ["npm", "run", "dev", "--", "--host"]