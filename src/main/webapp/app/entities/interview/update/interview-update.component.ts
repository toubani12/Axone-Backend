import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';
import { IApplication } from 'app/entities/application/application.model';
import { ApplicationService } from 'app/entities/application/service/application.service';
import { InterviewService } from '../service/interview.service';
import { IInterview } from '../interview.model';
import { InterviewFormService, InterviewFormGroup } from './interview-form.service';

@Component({
  standalone: true,
  selector: 'jhi-interview-update',
  templateUrl: './interview-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InterviewUpdateComponent implements OnInit {
  isSaving = false;
  interview: IInterview | null = null;

  candidatesSharedCollection: ICandidate[] = [];
  applicationsSharedCollection: IApplication[] = [];

  protected interviewService = inject(InterviewService);
  protected interviewFormService = inject(InterviewFormService);
  protected candidateService = inject(CandidateService);
  protected applicationService = inject(ApplicationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InterviewFormGroup = this.interviewFormService.createInterviewFormGroup();

  compareCandidate = (o1: ICandidate | null, o2: ICandidate | null): boolean => this.candidateService.compareCandidate(o1, o2);

  compareApplication = (o1: IApplication | null, o2: IApplication | null): boolean => this.applicationService.compareApplication(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interview }) => {
      this.interview = interview;
      if (interview) {
        this.updateForm(interview);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interview = this.interviewFormService.getInterview(this.editForm);
    if (interview.id !== null) {
      this.subscribeToSaveResponse(this.interviewService.update(interview));
    } else {
      this.subscribeToSaveResponse(this.interviewService.create(interview));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterview>>): void {
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

  protected updateForm(interview: IInterview): void {
    this.interview = interview;
    this.interviewFormService.resetForm(this.editForm, interview);

    this.candidatesSharedCollection = this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(
      this.candidatesSharedCollection,
      interview.attendee,
    );
    this.applicationsSharedCollection = this.applicationService.addApplicationToCollectionIfMissing<IApplication>(
      this.applicationsSharedCollection,
      interview.application,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.candidateService
      .query()
      .pipe(map((res: HttpResponse<ICandidate[]>) => res.body ?? []))
      .pipe(
        map((candidates: ICandidate[]) =>
          this.candidateService.addCandidateToCollectionIfMissing<ICandidate>(candidates, this.interview?.attendee),
        ),
      )
      .subscribe((candidates: ICandidate[]) => (this.candidatesSharedCollection = candidates));

    this.applicationService
      .query()
      .pipe(map((res: HttpResponse<IApplication[]>) => res.body ?? []))
      .pipe(
        map((applications: IApplication[]) =>
          this.applicationService.addApplicationToCollectionIfMissing<IApplication>(applications, this.interview?.application),
        ),
      )
      .subscribe((applications: IApplication[]) => (this.applicationsSharedCollection = applications));
  }
}
