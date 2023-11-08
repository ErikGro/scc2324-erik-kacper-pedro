import { faker } from '@faker-js/faker';
import * as fs from 'fs';

//////////////////// HELPER ////////////////////

export function objectToCommaSeparated(object) {
  return Object.keys(object).map(function (k) {
    return object[k].toString().replace(/,/g, '')
  }).join(",")
}

export function createCSVFromArray(array, filename) {
  const lines = array.map(i => objectToCommaSeparated(i))
  lines.unshift(Object.keys(array[0]).join(','))
  fs.writeFileSync("mock-data/" + filename, lines.join('\n'));
}

export function format(date) {
  return date.toISOString().split('T')[0]
}

export function getRandomInt(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function getRandomArbitrary(min, max) {
  return Math.random() * (max - min) + min;
}

export function availablePeriod() {
  const start = new Date(faker.date.soon())
  start.setDate(start.getDate() + getRandomInt(0, 40))
  const end = new Date(start.valueOf())
  end.setDate(end.getDate() + getRandomInt(20, 200))

  const normalPrice = getRandomArbitrary(20, 200)
  const promotionPricePerDay = getRandomArbitrary(20, normalPrice)

  return {
    startDate: format(start),
    endDate: format(end),
    normalPricePerDay: normalPrice,
    promotionPricePerDay: promotionPricePerDay
  }
}

//////////////////// CREATE MOCK DATA ////////////////////

export function createRandomUser() {
  return {
    username: faker.internet.userName(),
    password: faker.internet.password()
  };
}

export const users = faker.helpers.multiple(createRandomUser, {
  count: 50000
})

createCSVFromArray(users, 'users.csv')

export function createRandomHouse() {
  return {
    name: faker.animal.cat(),
    description: faker.commerce.productDescription(),
  };
}

export const houses = faker.helpers.multiple(createRandomHouse, {
  count: 1000
})

createCSVFromArray(houses, 'houses.csv')

export const periods = faker.helpers.multiple(availablePeriod, {
  count: 10
})

createCSVFromArray(periods, 'periods.csv')
