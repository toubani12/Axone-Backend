import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { ITechCVAchievement } from '../tech-cv-achievement.model';
import { TechCVAchievementService } from '../service/tech-cv-achievement.service';
import { TechCVAchievementFormService, TechCVAchievementFormGroup } from './tech-cv-achievement-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-achievement-update',
  templateUrl: './tech-cv-achievement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TechCVAchievementUpdateComponent implements OnInit {
  isSaving = false;
  techCVAchievement: ITechCVAchievement | null = null;

  technicalCVSSharedCollection: ITechnicalCV[] = [];

  protected techCVAchievementService = inject(TechCVAchievementService);
  protected techCVAchievementFormService = inject(TechCVAchievementFormService);
  protected technicalCVService = inject(TechnicalCVService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechCVAchievementFormGroup = this.techCVAchievementFormService.createTechCVAchievementFormGroup();

  compareTechnicalCV = (o1: ITechnicalCV | null, o2: ITechnicalCV | null): boolean => this.technicalCVService.compareTechnicalCV(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ techCVAchievement }) => {
      this.techCVAchievement = techCVAchievement;
      if (techCVAchievement) {
        this.updateForm(techCVAchievement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const techCVAchievement = this.techCVAchievementFormService.getTechCVAchievement(this.editForm);
    if (techCVAchievement.id !== null) {
      this.subscribeToSaveResponse(this.techCVAchievementService.update(techCVAchievement));
    } else {
      this.subscribeToSaveResponse(this.techCVAchievementService.create(techCVAchievement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechCVAchievement>>): void {
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

  protected updateForm(techCVAchievement: ITechCVAchievement): void {
    this.techCVAchievement = techCVAchievement;
    this.techCVAchievementFormService.resetForm(this.editForm, techCVAchievement);

    this.technicalCVSSharedCollection = this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(
      this.technicalCVSSharedCollection,
      techCVAchievement.technicalCV,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.technicalCVService
      .query()
      .pipe(map((res: HttpResponse<ITechnicalCV[]>) => res.body ?? []))
      .pipe(
        map((technicalCVS: ITechnicalCV[]) =>
          this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(technicalCVS, this.techCVAchievement?.technicalCV),
        ),
      )
      .subscribe((technicalCVS: ITechnicalCV[]) => (this.technicalCVSSharedCollection = technicalCVS));
  }
}
