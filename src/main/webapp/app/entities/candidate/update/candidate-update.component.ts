import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { IDomain } from 'app/entities/domain/domain.model';
import { DomainService } from 'app/entities/domain/service/domain.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { CandidateService } from '../service/candidate.service';
import { ICandidate } from '../candidate.model';
import { CandidateFormService, CandidateFormGroup } from './candidate-form.service';

@Component({
  standalone: true,
  selector: 'jhi-candidate-update',
  templateUrl: './candidate-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CandidateUpdateComponent implements OnInit {
  isSaving = false;
  candidate: ICandidate | null = null;

  techCVSCollection: ITechnicalCV[] = [];
  domainsSharedCollection: IDomain[] = [];
  applicationsSharedCollection: IApplication[] = [];

  protected candidateService = inject(CandidateService);
  protected candidateFormService = inject(CandidateFormService);
  protected technicalCVService = inject(TechnicalCVService);
  protected domainService = inject(DomainService);
  protected applicationService = inject(ApplicationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CandidateFormGroup = this.candidateFormService.createCandidateFormGroup();

  compareTechnicalCV = (o1: ITechnicalCV | null, o2: ITechnicalCV | null): boolean => this.technicalCVService.compareTechnicalCV(o1, o2);

  compareDomain = (o1: IDomain | null, o2: IDomain | null): boolean => this.domainService.compareDomain(o1, o2);

  compareApplication = (o1: IApplication | null, o2: IApplication | null): boolean => this.applicationService.compareApplication(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidate }) => {
      this.candidate = candidate;
      if (candidate) {
        this.updateForm(candidate);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const candidate = this.candidateFormService.getCandidate(this.editForm);
    if (candidate.id !== null) {
      this.subscribeToSaveResponse(this.candidateService.update(candidate));
    } else {
      this.subscribeToSaveResponse(this.candidateService.create(candidate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidate>>): void {
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

  protected updateForm(candidate: ICandidate): void {
    this.candidate = candidate;
    this.candidateFormService.resetForm(this.editForm, candidate);

    this.techCVSCollection = this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(
      this.techCVSCollection,
      candidate.techCV,
    );
    this.domainsSharedCollection = this.domainService.addDomainToCollectionIfMissing<IDomain>(
      this.domainsSharedCollection,
      ...(candidate.domains ?? []),
    );
    this.applicationsSharedCollection = this.applicationService.addApplicationToCollectionIfMissing<IApplication>(
      this.applicationsSharedCollection,
      ...(candidate.applications ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.technicalCVService
      .query({ filter: 'candidate-is-null' })
      .pipe(map((res: HttpResponse<ITechnicalCV[]>) => res.body ?? []))
      .pipe(
        map((technicalCVS: ITechnicalCV[]) =>
          this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(technicalCVS, this.candidate?.techCV),
        ),
      )
      .subscribe((technicalCVS: ITechnicalCV[]) => (this.techCVSCollection = technicalCVS));

    this.domainService
      .query()
      .pipe(map((res: HttpResponse<IDomain[]>) => res.body ?? []))
      .pipe(
        map((domains: IDomain[]) =>
          this.domainService.addDomainToCollectionIfMissing<IDomain>(domains, ...(this.candidate?.domains ?? [])),
        ),
      )
      .subscribe((domains: IDomain[]) => (this.domainsSharedCollection = domains));

    this.applicationService
      .query()
      .pipe(map((res: HttpResponse<IApplication[]>) => res.body ?? []))
      .pipe(
        map((applications: IApplication[]) =>
          this.applicationService.addApplicationToCollectionIfMissing<IApplication>(applications, ...(this.candidate?.applications ?? [])),
        ),
      )
      .subscribe((applications: IApplication[]) => (this.applicationsSharedCollection = applications));
  }
}
