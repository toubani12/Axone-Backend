import { TechCVLevel } from 'app/entities/enumerations/tech-cv-level.model';

export interface ITechnicalCV {
  id: number;
  name?: string | null;
  level?: keyof typeof TechCVLevel | null;
  profileDescription?: string | null;
}

export type NewTechnicalCV = Omit<ITechnicalCV, 'id'> & { id: null };
