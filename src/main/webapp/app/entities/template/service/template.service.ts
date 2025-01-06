import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITemplate, NewTemplate } from '../template.model';

export type PartialUpdateTemplate = Partial<ITemplate> & Pick<ITemplate, 'id'>;

export type EntityResponseType = HttpResponse<ITemplate>;
export type EntityArrayResponseType = HttpResponse<ITemplate[]>;

@Injectable({ providedIn: 'root' })
export class TemplateService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/templates');

  create(template: NewTemplate): Observable<EntityResponseType> {
    return this.http.post<ITemplate>(this.resourceUrl, template, { observe: 'response' });
  }

  update(template: ITemplate): Observable<EntityResponseType> {
    return this.http.put<ITemplate>(`${this.resourceUrl}/${this.getTemplateIdentifier(template)}`, template, { observe: 'response' });
  }

  partialUpdate(template: PartialUpdateTemplate): Observable<EntityResponseType> {
    return this.http.patch<ITemplate>(`${this.resourceUrl}/${this.getTemplateIdentifier(template)}`, template, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITemplate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITemplate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTemplateIdentifier(template: Pick<ITemplate, 'id'>): number {
    return template.id;
  }

  compareTemplate(o1: Pick<ITemplate, 'id'> | null, o2: Pick<ITemplate, 'id'> | null): boolean {
    return o1 && o2 ? this.getTemplateIdentifier(o1) === this.getTemplateIdentifier(o2) : o1 === o2;
  }

  addTemplateToCollectionIfMissing<Type extends Pick<ITemplate, 'id'>>(
    templateCollection: Type[],
    ...templatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const templates: Type[] = templatesToCheck.filter(isPresent);
    if (templates.length > 0) {
      const templateCollectionIdentifiers = templateCollection.map(templateItem => this.getTemplateIdentifier(templateItem));
      const templatesToAdd = templates.filter(templateItem => {
        const templateIdentifier = this.getTemplateIdentifier(templateItem);
        if (templateCollectionIdentifiers.includes(templateIdentifier)) {
          return false;
        }
        templateCollectionIdentifiers.push(templateIdentifier);
        return true;
      });
      return [...templatesToAdd, ...templateCollection];
    }
    return templateCollection;
  }
}
