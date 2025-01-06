import { ITechCVAchievement, NewTechCVAchievement } from './tech-cv-achievement.model';

export const sampleWithRequiredData: ITechCVAchievement = {
  id: 31558,
  achievement: 'stumble ferociously',
};

export const sampleWithPartialData: ITechCVAchievement = {
  id: 14261,
  achievement: 'past',
};

export const sampleWithFullData: ITechCVAchievement = {
  id: 2472,
  achievement: 'decline',
};

export const sampleWithNewData: NewTechCVAchievement = {
  achievement: 'gadzooks',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
