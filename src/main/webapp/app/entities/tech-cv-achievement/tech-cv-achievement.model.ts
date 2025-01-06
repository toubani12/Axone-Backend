import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';

export interface ITechCVAchievement {
  id: number;
  achievement?: string | null;
  technicalCV?: ITechnicalCV | null;
}

export type NewTechCVAchievement = Omit<ITechCVAchievement, 'id'> & { id: null };
