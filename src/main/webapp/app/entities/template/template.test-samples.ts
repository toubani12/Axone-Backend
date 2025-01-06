import { ITemplate, NewTemplate } from './template.model';

export const sampleWithRequiredData: ITemplate = {
  id: 24208,
  label: 'withhold',
  type: 'CDI',
  standard: true,
  docLink: '../fake-data/blob/hipster.png',
  docLinkContentType: 'unknown',
};

export const sampleWithPartialData: ITemplate = {
  id: 24348,
  label: 'galumph sequence duh',
  type: 'MISSION',
  description: 'foolishly',
  standard: true,
  docLink: '../fake-data/blob/hipster.png',
  docLinkContentType: 'unknown',
};

export const sampleWithFullData: ITemplate = {
  id: 20118,
  label: 'gracefully staple',
  type: 'FREELANCE',
  description: 'gall-bladder',
  standard: true,
  docLink: '../fake-data/blob/hipster.png',
  docLinkContentType: 'unknown',
};

export const sampleWithNewData: NewTemplate = {
  label: 'even',
  type: 'CDD',
  standard: false,
  docLink: '../fake-data/blob/hipster.png',
  docLinkContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
