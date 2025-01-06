import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEmployer } from 'app/entities/employer/employer.model';
import { EmployerService } from 'app/entities/employer/service/employer.service';
import { IRecruiter } from 'app/entities/recruiter/recruiter.model';
import { RecruiterService } from 'app/entities/recruiter/service/recruiter.service';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { NDAStatus } from 'app/entities/enumerations/nda-status.model';
import { NDAService } from '../service/nda.service';
import { INDA } from '../nda.model';
import { NDAFormService, NDAFormGroup } from './nda-form.service';

@Component({
  standalone: true,
  selector: 'jhi-nda-update',
  templateUrl: './nda-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NDAUpdateComponent implements OnInit {
  isSaving = false;
  nDA: INDA | null = null;
  nDAStatusValues = Object.keys(NDAStatus);

  employersSharedCollection: IEmployer[] = [];
  recruitersSharedCollection: IRecruiter[] = [];
  candidatesSharedCollection: ICandidate[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected nDAService = inject(NDAService);
  protected nDAFormService = inject(NDAFormService);
  protected employerService = inject(EmployerService);
  protected recruiterService = inject(RecruiterService);
  protected candidateService = inject(CandidateService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NDAFormGroup = this.nDAFormService.createNDAFormGroup();

  compareEmployer = (o1: IEmployer | null, o2: IEmployer | null): boolean => this.employerService.compareEmployer(o1, o2);

  compareRecruiter = (o1: IRecruiter | null, o2: IRecruiter | null): boolean => this.recruiterService.compareRecruiter(o1, o2);

  compareCandidate = (o1: ICandidate | null, o2: ICandidate | null): boolean => this.candidateService.compareCandidate(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nDA }) => {
      this.nDA = nDA;
      if (nDA) {
        this.updateForm(nDA);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('hrSolutionApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nDA = this.nDAFormService.getNDA(this.editForm);
    if (nDA.id !== null) {
      this.subscribeToSaveResponse(this.nDAService.update(nDA));
    } else {
      this.subscribeToSaveResponse(this.nDAService.create(nDA));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INDA>>): void {
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

  protected updateForm(nDA: INDA): void {
    this.nDA = nDA;
    this.nDAFormService.resetForm(this.editForm, nDA);

    this.employersSharedCollection = this.employerService.addEmployerToCollectionIfMissing<IEmployer>(
      this.employersSharedCollection,
      nDA.employer,
    );
    this.recruitersSharedCollection = this.recruiterService.addRecruiterToCollectionIfMissing<IRecruiter>(
      this.recruitersSharedCollection,
      nDA.mediator,
    );
    this.candidatesSharedCollection = this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(
      this.candidatesSharedCollection,
      nDA.candidate,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employerService
      .query()
      .pipe(map((res: HttpResponse<IEmployer[]>) => res.body ?? []))
      .pipe(
        map((employers: IEmployer[]) => this.employerService.addEmployerToCollectionIfMissing<IEmployer>(employers, this.nDA?.employer)),
      )
      .subscribe((employers: IEmployer[]) => (this.employersSharedCollection = employers));

    this.recruiterService
      .query()
      .pipe(map((res: HttpResponse<IRecruiter[]>) => res.body ?? []))
      .pipe(
        map((recruiters: IRecruiter[]) =>
          this.recruiterService.addRecruiterToCollectionIfMissing<IRecruiter>(recruiters, this.nDA?.mediator),
        ),
      )
      .subscribe((recruiters: IRecruiter[]) => (this.recruitersSharedCollection = recruiters));

    this.candidateService
      .query()
      .pipe(map((res: HttpResponse<ICandidate[]>) => res.body ?? []))
      .pipe(
        map((candidates: ICandidate[]) =>
          this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(candidates, this.nDA?.candidate),
        ),
      )
      .subscribe((candidates: ICandidate[]) => (this.candidatesSharedCollection = candidates));
  }
}
