import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';

export interface ITechCVDocs {
  id: number;
  attachedDoc?: string | null;
  attachedDocContentType?: string | null;
  technicalCV?: ITechnicalCV | null;
}

export type NewTechCVDocs = Omit<ITechCVDocs, 'id'> & { id: null };
