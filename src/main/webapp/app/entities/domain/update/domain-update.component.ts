import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RecruiterService } from 'app/entities/recruiter/service/recruiter.service';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { DomainService } from '../service/domain.service';
import { IDomain } from '../domain.model';
import { DomainFormService, DomainFormGroup } from './domain-form.service';

@Component({
  standalone: true,
  selector: 'jhi-domain-update',
  templateUrl: './domain-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DomainUpdateComponent implements OnInit {
  isSaving = false;
  domain: IDomain | null = null;

  recruitersSharedCollection: IRecruiter[] = [];
  candidatesSharedCollection: ICandidate[] = [];
  applicationsSharedCollection: IApplication[] = [];
  employersSharedCollection: IEmployer[] = [];

  protected domainService = inject(DomainService);
  protected domainFormService = inject(DomainFormService);
  protected recruiterService = inject(RecruiterService);
  protected candidateService = inject(CandidateService);
  protected applicationService = inject(ApplicationService);
  protected employerService = inject(EmployerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DomainFormGroup = this.domainFormService.createDomainFormGroup();

  compareRecruiter = (o1: IRecruiter | null, o2: IRecruiter | null): boolean => this.recruiterService.compareRecruiter(o1, o2);

  compareCandidate = (o1: ICandidate | null, o2: ICandidate | null): boolean => this.candidateService.compareCandidate(o1, o2);

  compareApplication = (o1: IApplication | null, o2: IApplication | null): boolean => this.applicationService.compareApplication(o1, o2);

  compareEmployer = (o1: IEmployer | null, o2: IEmployer | null): boolean => this.employerService.compareEmployer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domain }) => {
      this.domain = domain;
      if (domain) {
        this.updateForm(domain);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const domain = this.domainFormService.getDomain(this.editForm);
    if (domain.id !== null) {
      this.subscribeToSaveResponse(this.domainService.update(domain));
    } else {
      this.subscribeToSaveResponse(this.domainService.create(domain));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDomain>>): void {
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

  protected updateForm(domain: IDomain): void {
    this.domain = domain;
    this.domainFormService.resetForm(this.editForm, domain);

    this.recruitersSharedCollection = this.recruiterService.addRecruiterToCollectionIfMissing<IRecruiter>(
      this.recruitersSharedCollection,
      ...(domain.recruiters ?? []),
    );
    this.candidatesSharedCollection = this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(
      this.candidatesSharedCollection,
      ...(domain.candidates ?? []),
    );
    this.applicationsSharedCollection = this.applicationService.addApplicationToCollectionIfMissing<IApplication>(
      this.applicationsSharedCollection,
      ...(domain.applications ?? []),
    );
    this.employersSharedCollection = this.employerService.addEmployerToCollectionIfMissing<IEmployer>(
      this.employersSharedCollection,
      domain.employer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recruiterService
      .query()
      .pipe(map((res: HttpResponse<IRecruiter[]>) => res.body ?? []))
      .pipe(
        map((recruiters: IRecruiter[]) =>
          this.recruiterService.addRecruiterToCollectionIfMissing<IRecruiter>(recruiters, ...(this.domain?.recruiters ?? [])),
        ),
      )
      .subscribe((recruiters: IRecruiter[]) => (this.recruitersSharedCollection = recruiters));

    this.candidateService
      .query()
      .pipe(map((res: HttpResponse<ICandidate[]>) => res.body ?? []))
      .pipe(
        map((candidates: ICandidate[]) =>
          this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(candidates, ...(this.domain?.candidates ?? [])),
        ),
      )
      .subscribe((candidates: ICandidate[]) => (this.candidatesSharedCollection = candidates));

    this.applicationService
      .query()
      .pipe(map((res: HttpResponse<IApplication[]>) => res.body ?? []))
      .pipe(
        map((applications: IApplication[]) =>
          this.applicationService.addApplicationToCollectionIfMissing<IApplication>(applications, ...(this.domain?.applications ?? [])),
        ),
      )
      .subscribe((applications: IApplication[]) => (this.applicationsSharedCollection = applications));

    this.employerService
      .query()
      .pipe(map((res: HttpResponse<IEmployer[]>) => res.body ?? []))
      .pipe(
        map((employers: IEmployer[]) => this.employerService.addEmployerToCollectionIfMissing<IEmployer>(employers, this.domain?.employer)),
      )
      .subscribe((employers: IEmployer[]) => (this.employersSharedCollection = employers));
  }
}
