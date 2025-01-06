import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechCVSoftSkills, NewTechCVSoftSkills } from '../tech-cv-soft-skills.model';

export type PartialUpdateTechCVSoftSkills = Partial<ITechCVSoftSkills> & Pick<ITechCVSoftSkills, 'id'>;

export type EntityResponseType = HttpResponse<ITechCVSoftSkills>;
export type EntityArrayResponseType = HttpResponse<ITechCVSoftSkills[]>;

@Injectable({ providedIn: 'root' })
export class TechCVSoftSkillsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tech-cv-soft-skills');

  create(techCVSoftSkills: NewTechCVSoftSkills): Observable<EntityResponseType> {
    return this.http.post<ITechCVSoftSkills>(this.resourceUrl, techCVSoftSkills, { observe: 'response' });
  }

  update(techCVSoftSkills: ITechCVSoftSkills): Observable<EntityResponseType> {
    return this.http.put<ITechCVSoftSkills>(
      `${this.resourceUrl}/${this.getTechCVSoftSkillsIdentifier(techCVSoftSkills)}`,
      techCVSoftSkills,
      { observe: 'response' },
    );
  }

  partialUpdate(techCVSoftSkills: PartialUpdateTechCVSoftSkills): Observable<EntityResponseType> {
    return this.http.patch<ITechCVSoftSkills>(
      `${this.resourceUrl}/${this.getTechCVSoftSkillsIdentifier(techCVSoftSkills)}`,
      techCVSoftSkills,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechCVSoftSkills>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechCVSoftSkills[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechCVSoftSkillsIdentifier(techCVSoftSkills: Pick<ITechCVSoftSkills, 'id'>): number {
    return techCVSoftSkills.id;
  }

  compareTechCVSoftSkills(o1: Pick<ITechCVSoftSkills, 'id'> | null, o2: Pick<ITechCVSoftSkills, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechCVSoftSkillsIdentifier(o1) === this.getTechCVSoftSkillsIdentifier(o2) : o1 === o2;
  }

  addTechCVSoftSkillsToCollectionIfMissing<Type extends Pick<ITechCVSoftSkills, 'id'>>(
    techCVSoftSkillsCollection: Type[],
    ...techCVSoftSkillsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const techCVSoftSkills: Type[] = techCVSoftSkillsToCheck.filter(isPresent);
    if (techCVSoftSkills.length > 0) {
      const techCVSoftSkillsCollectionIdentifiers = techCVSoftSkillsCollection.map(techCVSoftSkillsItem =>
        this.getTechCVSoftSkillsIdentifier(techCVSoftSkillsItem),
      );
      const techCVSoftSkillsToAdd = techCVSoftSkills.filter(techCVSoftSkillsItem => {
        const techCVSoftSkillsIdentifier = this.getTechCVSoftSkillsIdentifier(techCVSoftSkillsItem);
        if (techCVSoftSkillsCollectionIdentifiers.includes(techCVSoftSkillsIdentifier)) {
          return false;
        }
        techCVSoftSkillsCollectionIdentifiers.push(techCVSoftSkillsIdentifier);
        return true;
      });
      return [...techCVSoftSkillsToAdd, ...techCVSoftSkillsCollection];
    }
    return techCVSoftSkillsCollection;
  }
}
