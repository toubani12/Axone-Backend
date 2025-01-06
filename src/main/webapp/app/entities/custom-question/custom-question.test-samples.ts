import { ICustomQuestion, NewCustomQuestion } from './custom-question.model';

export const sampleWithRequiredData: ICustomQuestion = {
  id: 10322,
  question: 'weekend overconfidently upon',
};

export const sampleWithPartialData: ICustomQuestion = {
  id: 11047,
  question: 'intently along',
};

export const sampleWithFullData: ICustomQuestion = {
  id: 30333,
  question: 'jalapeño monkey spiral',
  answer: 'wherever merry',
  correctAnswer: 'zealous',
};

export const sampleWithNewData: NewCustomQuestion = {
  question: 'poorly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
