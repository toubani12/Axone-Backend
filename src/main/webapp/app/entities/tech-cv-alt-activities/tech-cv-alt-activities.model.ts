import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';

export interface ITechCVAltActivities {
  id: number;
  activities?: string | null;
  technicalCV?: ITechnicalCV | null;
}

export type NewTechCVAltActivities = Omit<ITechCVAltActivities, 'id'> & { id: null };
