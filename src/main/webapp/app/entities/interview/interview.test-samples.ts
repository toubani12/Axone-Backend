import dayjs from 'dayjs/esm';

import { IInterview, NewInterview } from './interview.model';

export const sampleWithRequiredData: IInterview = {
  id: 19075,
  label: 'zowie acquaintance',
  entryLink: 'worker',
  invitationLink: 'eagle lovingly boohoo',
};

export const sampleWithPartialData: IInterview = {
  id: 31455,
  label: 'rapidly',
  entryLink: 'plus paltry',
  invitationLink: 'brocolli cruelly cenotaph',
  rate: 23767.39,
};

export const sampleWithFullData: IInterview = {
  id: 21101,
  label: 'signify idolized',
  entryLink: 'tricky',
  invitationLink: 'after canvass',
  interviewResult: true,
  rate: 28296.61,
  startedAt: dayjs('2024-05-23'),
  endedAt: dayjs('2024-05-24'),
  comments: 'gadzooks function',
};

export const sampleWithNewData: NewInterview = {
  label: 'number ick unsung',
  entryLink: 'consequently punctually',
  invitationLink: 'gadzooks',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
