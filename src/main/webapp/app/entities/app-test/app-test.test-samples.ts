import { IAppTest, NewAppTest } from './app-test.model';

export const sampleWithRequiredData: IAppTest = {
  id: 16397,
  name: 'furthermore fragrant',
  invitationLink: 'minus',
};

export const sampleWithPartialData: IAppTest = {
  id: 26874,
  name: 'throughout irritably',
  invitationLink: 'toward upright',
  completed: false,
  algorithm: 'constitution astride',
  isCodeTest: true,
};

export const sampleWithFullData: IAppTest = {
  id: 6302,
  name: 'thankfully anguished',
  invitationLink: 'consequently period acclimatise',
  totalScore: 16494,
  status: 'tender',
  completed: false,
  testID: 21857,
  algorithm: 'why save',
  isCodeTest: true,
  duration: 27512,
};

export const sampleWithNewData: NewAppTest = {
  name: 'or meek',
  invitationLink: 'without waistband than',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
