import { ITechCVDocs, NewTechCVDocs } from './tech-cv-docs.model';

export const sampleWithRequiredData: ITechCVDocs = {
  id: 28312,
  attachedDoc: '../fake-data/blob/hipster.png',
  attachedDocContentType: 'unknown',
};

export const sampleWithPartialData: ITechCVDocs = {
  id: 16451,
  attachedDoc: '../fake-data/blob/hipster.png',
  attachedDocContentType: 'unknown',
};

export const sampleWithFullData: ITechCVDocs = {
  id: 26768,
  attachedDoc: '../fake-data/blob/hipster.png',
  attachedDocContentType: 'unknown',
};

export const sampleWithNewData: NewTechCVDocs = {
  attachedDoc: '../fake-data/blob/hipster.png',
  attachedDocContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
