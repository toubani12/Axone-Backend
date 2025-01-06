import { ITechCVAltActivities, NewTechCVAltActivities } from './tech-cv-alt-activities.model';

export const sampleWithRequiredData: ITechCVAltActivities = {
  id: 8859,
  activities: 'aha phooey',
};

export const sampleWithPartialData: ITechCVAltActivities = {
  id: 13641,
  activities: 'wetly',
};

export const sampleWithFullData: ITechCVAltActivities = {
  id: 11816,
  activities: 'input supposing',
};

export const sampleWithNewData: NewTechCVAltActivities = {
  activities: 'and homework',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
