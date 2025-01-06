import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICandidate, NewCandidate } from '../candidate.model';

export type PartialUpdateCandidate = Partial<ICandidate> & Pick<ICandidate, 'id'>;

export type EntityResponseType = HttpResponse<ICandidate>;
export type EntityArrayResponseType = HttpResponse<ICandidate[]>;

@Injectable({ providedIn: 'root' })
export class CandidateService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/candidates');

  create(candidate: NewCandidate): Observable<EntityResponseType> {
    return this.http.post<ICandidate>(this.resourceUrl, candidate, { observe: 'response' });
  }

  update(candidate: ICandidate): Observable<EntityResponseType> {
    return this.http.put<ICandidate>(`${this.resourceUrl}/${this.getCandidateIdentifier(candidate)}`, candidate, { observe: 'response' });
  }

  partialUpdate(candidate: PartialUpdateCandidate): Observable<EntityResponseType> {
    return this.http.patch<ICandidate>(`${this.resourceUrl}/${this.getCandidateIdentifier(candidate)}`, candidate, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICandidate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICandidate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCandidateIdentifier(candidate: Pick<ICandidate, 'id'>): number {
    return candidate.id;
  }

  compareCandidate(o1: Pick<ICandidate, 'id'> | null, o2: Pick<ICandidate, 'id'> | null): boolean {
    return o1 && o2 ? this.getCandidateIdentifier(o1) === this.getCandidateIdentifier(o2) : o1 === o2;
  }

  addCandidateToCollectionIfMissing<Type extends Pick<ICandidate, 'id'>>(
    candidateCollection: Type[],
    ...candidatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const candidates: Type[] = candidatesToCheck.filter(isPresent);
    if (candidates.length > 0) {
      const candidateCollectionIdentifiers = candidateCollection.map(candidateItem => this.getCandidateIdentifier(candidateItem));
      const candidatesToAdd = candidates.filter(candidateItem => {
        const candidateIdentifier = this.getCandidateIdentifier(candidateItem);
        if (candidateCollectionIdentifiers.includes(candidateIdentifier)) {
          return false;
        }
        candidateCollectionIdentifiers.push(candidateIdentifier);
        return true;
      });
      return [...candidatesToAdd, ...candidateCollection];
    }
    return candidateCollection;
  }
}
