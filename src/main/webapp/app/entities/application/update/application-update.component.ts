import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContractType } from 'app/entities/contract-type/contract-type.model';
import { ContractTypeService } from 'app/entities/contract-type/service/contract-type.service';
import { ITemplate } from 'app/entities/template/template.model';
import { TemplateService } from 'app/entities/template/service/template.service';
import { ICriteria } from 'app/entities/criteria/criteria.model';
import { CriteriaService } from 'app/entities/criteria/service/criteria.service';
import { IDomain } from 'app/entities/domain/domain.model';
import { DomainService } from 'app/entities/domain/service/domain.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RecruiterService } from 'app/entities/recruiter/service/recruiter.service';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { ApplicationStatus } from 'app/entities/enumerations/application-status.model';
import { ApplicationService } from '../service/application.service';
import { IApplication } from '../application.model';
import { ApplicationFormService, ApplicationFormGroup } from './application-form.service';

@Component({
  standalone: true,
  selector: 'jhi-application-update',
  templateUrl: './application-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ApplicationUpdateComponent implements OnInit {
  isSaving = false;
  application: IApplication | null = null;
  applicationStatusValues = Object.keys(ApplicationStatus);

  contractTypesSharedCollection: IContractType[] = [];
  templatesSharedCollection: ITemplate[] = [];
  criteriaSharedCollection: ICriteria[] = [];
  domainsSharedCollection: IDomain[] = [];
  employersSharedCollection: IEmployer[] = [];
  recruitersSharedCollection: IRecruiter[] = [];
  candidatesSharedCollection: ICandidate[] = [];

  protected applicationService = inject(ApplicationService);
  protected applicationFormService = inject(ApplicationFormService);
  protected contractTypeService = inject(ContractTypeService);
  protected templateService = inject(TemplateService);
  protected criteriaService = inject(CriteriaService);
  protected domainService = inject(DomainService);
  protected employerService = inject(EmployerService);
  protected recruiterService = inject(RecruiterService);
  protected candidateService = inject(CandidateService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ApplicationFormGroup = this.applicationFormService.createApplicationFormGroup();

  compareContractType = (o1: IContractType | null, o2: IContractType | null): boolean =>
    this.contractTypeService.compareContractType(o1, o2);

  compareTemplate = (o1: ITemplate | null, o2: ITemplate | null): boolean => this.templateService.compareTemplate(o1, o2);

  compareCriteria = (o1: ICriteria | null, o2: ICriteria | null): boolean => this.criteriaService.compareCriteria(o1, o2);

  compareDomain = (o1: IDomain | null, o2: IDomain | null): boolean => this.domainService.compareDomain(o1, o2);

  compareEmployer = (o1: IEmployer | null, o2: IEmployer | null): boolean => this.employerService.compareEmployer(o1, o2);

  compareRecruiter = (o1: IRecruiter | null, o2: IRecruiter | null): boolean => this.recruiterService.compareRecruiter(o1, o2);

  compareCandidate = (o1: ICandidate | null, o2: ICandidate | null): boolean => this.candidateService.compareCandidate(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ application }) => {
      this.application = application;
      if (application) {
        this.updateForm(application);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const application = this.applicationFormService.getApplication(this.editForm);
    if (application.id !== null) {
      this.subscribeToSaveResponse(this.applicationService.update(application));
    } else {
      this.subscribeToSaveResponse(this.applicationService.create(application));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplication>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(application: IApplication): void {
    this.application = application;
    this.applicationFormService.resetForm(this.editForm, application);

    this.contractTypesSharedCollection = this.contractTypeService.addContractTypeToCollectionIfMissing<IContractType>(
      this.contractTypesSharedCollection,
      ...(application.contractTypes ?? []),
    );
    this.templatesSharedCollection = this.templateService.addTemplateToCollectionIfMissing<ITemplate>(
      this.templatesSharedCollection,
      ...(application.contractTemplates ?? []),
    );
    this.criteriaSharedCollection = this.criteriaService.addCriteriaToCollectionIfMissing<ICriteria>(
      this.criteriaSharedCollection,
      ...(application.criteria ?? []),
    );
    this.domainsSharedCollection = this.domainService.addDomainToCollectionIfMissing<IDomain>(
      this.domainsSharedCollection,
      ...(application.domains ?? []),
    );
    this.employersSharedCollection = this.employerService.addEmployerToCollectionIfMissing<IEmployer>(
      this.employersSharedCollection,
      application.employer,
    );
    this.recruitersSharedCollection = this.recruiterService.addRecruiterToCollectionIfMissing<IRecruiter>(
      this.recruitersSharedCollection,
      ...(application.recruiters ?? []),
    );
    this.candidatesSharedCollection = this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(
      this.candidatesSharedCollection,
      ...(application.candidates ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractTypeService
      .query()
      .pipe(map((res: HttpResponse<IContractType[]>) => res.body ?? []))
      .pipe(
        map((contractTypes: IContractType[]) =>
          this.contractTypeService.addContractTypeToCollectionIfMissing<IContractType>(
            contractTypes,
            ...(this.application?.contractTypes ?? []),
          ),
        ),
      )
      .subscribe((contractTypes: IContractType[]) => (this.contractTypesSharedCollection = contractTypes));

    this.templateService
      .query()
      .pipe(map((res: HttpResponse<ITemplate[]>) => res.body ?? []))
      .pipe(
        map((templates: ITemplate[]) =>
          this.templateService.addTemplateToCollectionIfMissing<ITemplate>(templates, ...(this.application?.contractTemplates ?? [])),
        ),
      )
      .subscribe((templates: ITemplate[]) => (this.templatesSharedCollection = templates));

    this.criteriaService
      .query()
      .pipe(map((res: HttpResponse<ICriteria[]>) => res.body ?? []))
      .pipe(
        map((criteria: ICriteria[]) =>
          this.criteriaService.addCriteriaToCollectionIfMissing<ICriteria>(criteria, ...(this.application?.criteria ?? [])),
        ),
      )
      .subscribe((criteria: ICriteria[]) => (this.criteriaSharedCollection = criteria));

    this.domainService
      .query()
      .pipe(map((res: HttpResponse<IDomain[]>) => res.body ?? []))
      .pipe(
        map((domains: IDomain[]) =>
          this.domainService.addDomainToCollectionIfMissing<IDomain>(domains, ...(this.application?.domains ?? [])),
        ),
      )
      .subscribe((domains: IDomain[]) => (this.domainsSharedCollection = domains));

    this.employerService
      .query()
      .pipe(map((res: HttpResponse<IEmployer[]>) => res.body ?? []))
      .pipe(
        map((employers: IEmployer[]) =>
          this.employerService.addEmployerToCollectionIfMissing<IEmployer>(employers, this.application?.employer),
        ),
      )
      .subscribe((employers: IEmployer[]) => (this.employersSharedCollection = employers));

    this.recruiterService
      .query()
      .pipe(map((res: HttpResponse<IRecruiter[]>) => res.body ?? []))
      .pipe(
        map((recruiters: IRecruiter[]) =>
          this.recruiterService.addRecruiterToCollectionIfMissing<IRecruiter>(recruiters, ...(this.application?.recruiters ?? [])),
        ),
      )
      .subscribe((recruiters: IRecruiter[]) => (this.recruitersSharedCollection = recruiters));

    this.candidateService
      .query()
      .pipe(map((res: HttpResponse<ICandidate[]>) => res.body ?? []))
      .pipe(
        map((candidates: ICandidate[]) =>
          this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(candidates, ...(this.application?.candidates ?? [])),
        ),
      )
      .subscribe((candidates: ICandidate[]) => (this.candidatesSharedCollection = candidates));
  }
}
