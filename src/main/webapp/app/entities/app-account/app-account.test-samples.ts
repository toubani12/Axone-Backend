import dayjs from 'dayjs/esm';

import { IAppAccount, NewAppAccount } from './app-account.model';

export const sampleWithRequiredData: IAppAccount = {
  id: 30640,
  accountNumber: 28478,
};

export const sampleWithPartialData: IAppAccount = {
  id: 10587,
  accountNumber: 5223,
};

export const sampleWithFullData: IAppAccount = {
  id: 5686,
  accountNumber: 11105,
  cardNumber: 8514,
  endDate: dayjs('2024-05-23'),
  cvv: 19955,
};

export const sampleWithNewData: NewAppAccount = {
  accountNumber: 21859,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
