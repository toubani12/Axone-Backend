import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecruiter, NewRecruiter } from '../recruiter.model';

export type PartialUpdateRecruiter = Partial<IRecruiter> & Pick<IRecruiter, 'id'>;

export type EntityResponseType = HttpResponse<IRecruiter>;
export type EntityArrayResponseType = HttpResponse<IRecruiter[]>;

@Injectable({ providedIn: 'root' })
export class RecruiterService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recruiters');

  create(recruiter: NewRecruiter): Observable<EntityResponseType> {
    return this.http.post<IRecruiter>(this.resourceUrl, recruiter, { observe: 'response' });
  }

  update(recruiter: IRecruiter): Observable<EntityResponseType> {
    return this.http.put<IRecruiter>(`${this.resourceUrl}/${this.getRecruiterIdentifier(recruiter)}`, recruiter, { observe: 'response' });
  }

  partialUpdate(recruiter: PartialUpdateRecruiter): Observable<EntityResponseType> {
    return this.http.patch<IRecruiter>(`${this.resourceUrl}/${this.getRecruiterIdentifier(recruiter)}`, recruiter, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRecruiter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRecruiter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRecruiterIdentifier(recruiter: Pick<IRecruiter, 'id'>): number {
    return recruiter.id;
  }

  compareRecruiter(o1: Pick<IRecruiter, 'id'> | null, o2: Pick<IRecruiter, 'id'> | null): boolean {
    return o1 && o2 ? this.getRecruiterIdentifier(o1) === this.getRecruiterIdentifier(o2) : o1 === o2;
  }

  addRecruiterToCollectionIfMissing<Type extends Pick<IRecruiter, 'id'>>(
    recruiterCollection: Type[],
    ...recruitersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const recruiters: Type[] = recruitersToCheck.filter(isPresent);
    if (recruiters.length > 0) {
      const recruiterCollectionIdentifiers = recruiterCollection.map(recruiterItem => this.getRecruiterIdentifier(recruiterItem));
      const recruitersToAdd = recruiters.filter(recruiterItem => {
        const recruiterIdentifier = this.getRecruiterIdentifier(recruiterItem);
        if (recruiterCollectionIdentifiers.includes(recruiterIdentifier)) {
          return false;
        }
        recruiterCollectionIdentifiers.push(recruiterIdentifier);
        return true;
      });
      return [...recruitersToAdd, ...recruiterCollection];
    }
    return recruiterCollection;
  }
}
