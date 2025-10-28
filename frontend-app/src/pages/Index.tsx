import { Button } from "@/components/ui/button";
import { ArrowRight, Mic, Brain, Calendar, Zap, CheckCircle2 } from "lucide-react";
import { useNavigate } from "react-router-dom";

const Index = () => {
  const navigate = useNavigate();

  const features = [
    {
      icon: Mic,
      title: "Smart Transcription",
      description: "AI-powered multi-language transcription with speaker detection",
    },
    {
      icon: Brain,
      title: "GTD Task Management",
      description: "Automatic extraction and organization of action items",
    },
    {
      icon: Calendar,
      title: "Pomodoro Time-Blocking",
      description: "Auto-schedule focus sessions with smart calendar integration",
    },
    {
      icon: Zap,
      title: "Instant MoM",
      description: "Professional minutes of meeting generated in seconds",
    },
  ];

  return (
    <div className="min-h-screen bg-background">
      {/* Hero Section */}
      <div className="relative overflow-hidden">
        <div className="absolute inset-0 bg-[var(--gradient-hero)]" />
        <div className="container relative mx-auto px-4 py-20">
          <div className="max-w-4xl mx-auto text-center">
            {/* Logo */}
            <div className="inline-flex items-center gap-3 mb-8 animate-in">
              <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-primary to-primary/80 flex items-center justify-center shadow-[var(--shadow-glow)]">
                <span className="text-4xl">🦗</span>
              </div>
              <h1 className="text-5xl font-bold">PMantis</h1>
            </div>

            {/* Tagline */}
            <h2 className="text-4xl md:text-5xl font-bold mb-6 animate-in">
              Transform Meetings into
              <span className="text-gradient block mt-2">Actionable Results</span>
            </h2>

            <p className="text-xl text-muted-foreground mb-8 max-w-2xl mx-auto animate-in">
              AI-powered meeting assistant that transcribes, organizes, and schedules your action items automatically. 
              Focus on what matters, let PMantis handle the rest.
            </p>

            {/* CTA */}
            <div className="flex flex-col sm:flex-row gap-4 justify-center mb-12 animate-in">
              <Button 
                size="lg" 
                className="text-lg px-8 shadow-[var(--shadow-glow)]"
                onClick={() => navigate('/dashboard')}
              >
                Get Started <ArrowRight className="ml-2 w-5 h-5" />
              </Button>
              <Button size="lg" variant="outline" className="text-lg px-8">
                Watch Demo
              </Button>
            </div>

            {/* Trust Indicators */}
            <div className="flex flex-wrap items-center justify-center gap-6 text-sm text-muted-foreground">
              <div className="flex items-center gap-2">
                <CheckCircle2 className="w-4 h-4 text-primary" />
                Multi-language Support
              </div>
              <div className="flex items-center gap-2">
                <CheckCircle2 className="w-4 h-4 text-primary" />
                GTD Methodology
              </div>
              <div className="flex items-center gap-2">
                <CheckCircle2 className="w-4 h-4 text-primary" />
                Enterprise Ready
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="container mx-auto px-4 py-20">
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold mb-4">Everything You Need</h2>
          <p className="text-lg text-muted-foreground">Powerful features designed for modern productivity</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {features.map((feature, index) => {
            const Icon = feature.icon;
            return (
              <div
                key={index}
                className="p-6 rounded-xl border-2 border-border bg-card hover:border-primary/50 hover:shadow-lg transition-all duration-300"
              >
                <div className="w-12 h-12 rounded-lg bg-primary/10 flex items-center justify-center mb-4">
                  <Icon className="w-6 h-6 text-primary" />
                </div>
                <h3 className="text-lg font-semibold mb-2">{feature.title}</h3>
                <p className="text-sm text-muted-foreground">{feature.description}</p>
              </div>
            );
          })}
        </div>
      </div>

      {/* How It Works */}
      <div className="bg-muted/30 py-20">
        <div className="container mx-auto px-4">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold mb-4">How It Works</h2>
            <p className="text-lg text-muted-foreground">From meeting to action in three simple steps</p>
          </div>

          <div className="max-w-4xl mx-auto">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
              <div className="text-center">
                <div className="w-16 h-16 rounded-full bg-primary text-primary-foreground flex items-center justify-center text-2xl font-bold mx-auto mb-4">
                  1
                </div>
                <h3 className="text-xl font-semibold mb-2">Record or Upload</h3>
                <p className="text-muted-foreground">Capture your meeting audio directly or import existing recordings</p>
              </div>

              <div className="text-center">
                <div className="w-16 h-16 rounded-full bg-primary text-primary-foreground flex items-center justify-center text-2xl font-bold mx-auto mb-4">
                  2
                </div>
                <h3 className="text-xl font-semibold mb-2">AI Processing</h3>
                <p className="text-muted-foreground">Get transcription, MoM, and action items automatically extracted</p>
              </div>

              <div className="text-center">
                <div className="w-16 h-16 rounded-full bg-primary text-primary-foreground flex items-center justify-center text-2xl font-bold mx-auto mb-4">
                  3
                </div>
                <h3 className="text-xl font-semibold mb-2">Take Action</h3>
                <p className="text-muted-foreground">Auto-schedule tasks, sync to calendar, and track completion</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* CTA Section */}
      <div className="container mx-auto px-4 py-20">
        <div className="max-w-3xl mx-auto text-center">
          <h2 className="text-3xl font-bold mb-4">Ready to Transform Your Meetings?</h2>
          <p className="text-lg text-muted-foreground mb-8">
            Join productivity-focused teams using PMantis to stay organized and focused
          </p>
          <Button 
            size="lg" 
            className="text-lg px-8 shadow-[var(--shadow-glow)]"
            onClick={() => navigate('/dashboard')}
          >
            Start Free Trial <ArrowRight className="ml-2 w-5 h-5" />
          </Button>
        </div>
      </div>

      {/* Footer */}
      <footer className="border-t border-border py-8">
        <div className="container mx-auto px-4">
          <div className="flex flex-col md:flex-row items-center justify-between gap-4">
            <div className="flex items-center gap-2">
              <span className="text-2xl">🦗</span>
              <span className="font-semibold">PMantis</span>
            </div>
            <p className="text-sm text-muted-foreground">
              © 2025 PMantis. AI Meeting Assistant.
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Index;
