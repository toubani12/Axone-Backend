import dayjs from 'dayjs/esm';

import { IApplication, NewApplication } from './application.model';

export const sampleWithRequiredData: IApplication = {
  id: 18169,
  title: 'to yowza',
  description: 'slow',
  numberOfCandidates: 13925,
  paymentAmount: 9974.61,
  status: 'OPEN',
};

export const sampleWithPartialData: IApplication = {
  id: 29405,
  title: 'where up',
  description: 'lest phew realistic',
  numberOfCandidates: 21594,
  paymentAmount: 7886.8,
  candidateIncomeRate: 22162.64,
  status: 'NEW',
};

export const sampleWithFullData: IApplication = {
  id: 512,
  title: 'pince-nez like',
  description: 'foolishly mileage while',
  numberOfCandidates: 11421,
  paymentAmount: 4375.4,
  recruiterIncomeRate: 10719.33,
  candidateIncomeRate: 16396.51,
  deadline: dayjs('2024-05-24'),
  status: 'CLOSED',
  createdAt: dayjs('2024-05-24T03:25'),
  openedAt: dayjs('2024-05-23T15:13'),
  closedAt: dayjs('2024-05-24T11:20'),
  doneAt: dayjs('2024-05-24T10:18'),
};

export const sampleWithNewData: NewApplication = {
  title: 'aw nick',
  description: 'about yowza',
  numberOfCandidates: 4832,
  paymentAmount: 23454.98,
  status: 'CLOSED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
