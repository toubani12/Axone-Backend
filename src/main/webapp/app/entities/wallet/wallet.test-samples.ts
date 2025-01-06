import { IWallet, NewWallet } from './wallet.model';

export const sampleWithRequiredData: IWallet = {
  id: 21837,
  balance: 11347.41,
  status: 'DEACTIVATED',
};

export const sampleWithPartialData: IWallet = {
  id: 28243,
  balance: 17569.65,
  status: 'DEACTIVATED',
};

export const sampleWithFullData: IWallet = {
  id: 10948,
  balance: 13238.24,
  status: 'ACTIVE',
};

export const sampleWithNewData: NewWallet = {
  balance: 19380.91,
  status: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
