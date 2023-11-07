import { faker } from '@faker-js/faker';
import * as fs from 'fs';

export function createRandomUser() {
  return {
    username: faker.internet.userName(),
    password: faker.internet.password()
  };
}

export const users = faker.helpers.multiple(createRandomUser, {
  count: 1000
});

export function objectToCommaSeparated(object) {
  return Object.keys(object).map(function(k){ 
    return object[k]
  }).join(",")
}

const usersStrings = users.map(user => objectToCommaSeparated(user))

fs.writeFileSync('users.csv', usersStrings.join('\n'));
